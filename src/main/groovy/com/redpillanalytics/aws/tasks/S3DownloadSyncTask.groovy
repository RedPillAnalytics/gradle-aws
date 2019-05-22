package com.redpillanalytics.aws.tasks

import com.amazonaws.services.s3.transfer.MultipleFileDownload
import groovy.util.logging.Slf4j
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

@Slf4j
class S3DownloadSyncTask extends S3Task {

   S3DownloadSyncTask() {
      description = 'Synchronize a directory to an S3 bucket.'
      group = 'AWS'
      outputs.upToDateWhen { false }
   }

   /**
    * When defined, subdirectories are not included, which is the default.
    */
   @Input
   @Option(option = 'no-recursive',
           description = 'When defined, subdirectories are not included, which is the default.'
   )
   boolean noRecursive

   @OutputDirectory
   File getOutputDir() {
      return project.file(filePath ?: '.')
   }

   @TaskAction
   def s3Sync() {
      MultipleFileDownload mfu = tm.downloadDirectory(bucketName, keyName, outputDir)
      mfu.waitForCompletion()
   }
}
