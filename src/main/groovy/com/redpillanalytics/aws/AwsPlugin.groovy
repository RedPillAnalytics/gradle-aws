package com.redpillanalytics.aws

import com.redpillanalytics.aws.tasks.S3DownloadSyncTask
import com.redpillanalytics.aws.tasks.S3DownloadTask
import com.redpillanalytics.aws.tasks.S3UploadSyncTask
import com.redpillanalytics.aws.tasks.S3UploadTask
import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project

@Slf4j
class AwsPlugin implements Plugin<Project> {

   void apply(Project project) {

      project.apply plugin: 'com.redpillanalytics.plugin-template'

      project.configure(project) {
         extensions.create('aws', AwsPluginExtension)
      }

      project.afterEvaluate {

         project.task('s3Download', type: S3DownloadTask) {}
         project.task('s3Upload', type: S3UploadTask) {}
         project.task('s3UploadSync', type: S3UploadSyncTask) {}
         project.task('s3DownloadSync', type: S3DownloadSyncTask) {}
      }
      // end of afterEvaluate
   }
}

