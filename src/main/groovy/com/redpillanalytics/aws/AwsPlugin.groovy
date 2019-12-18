package com.redpillanalytics.aws

import com.redpillanalytics.aws.containers.TaskGroupContainer
import com.redpillanalytics.aws.tasks.S3DownloadSyncTask
import com.redpillanalytics.aws.tasks.S3DownloadTask
import com.redpillanalytics.aws.tasks.S3UploadSyncTask
import com.redpillanalytics.aws.tasks.S3UploadTask
import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project

@Slf4j
class AwsPlugin implements Plugin<Project> {

   static final String EXTENSION = 'aws'

   void apply(Project project) {

      project.apply plugin: 'com.redpillanalytics.gradle-properties'

      project.configure(project) {
         extensions.create(EXTENSION, AwsPluginExtension)
         project."$EXTENSION".extensions.configs = project.container(TaskGroupContainer)
         project.extensions."$EXTENSION".configs.add(new TaskGroupContainer('default'))
      }

      project.afterEvaluate {

         project.extensions.pluginProps.setParameters(project, EXTENSION)

         project."$EXTENSION".configs.all { tg ->

            if (tg.isDefaultTask()) {
               project.task('s3Download', type: S3DownloadTask) {}
               project.task('s3Upload', type: S3UploadTask) {}
            }

            project.task(tg.getTaskName('s3UploadSync'), type: S3UploadSyncTask) {
               bucketName tg.bucket
               keyName tg.key
               filePath tg.path
            }
            project.task(tg.getTaskName('s3DownloadSync'), type: S3DownloadSyncTask) {
               bucketName tg.bucket
               keyName tg.key
               filePath tg.path
            }
         }
      }
      // end of afterEvaluate
   }
}

