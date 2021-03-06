package com.redpillanalytics.aws.tasks

import com.amazonaws.services.s3.transfer.MultipleFileUpload
import groovy.util.logging.Slf4j
import org.gradle.api.tasks.Input

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

@Slf4j
class S3UploadSyncTask extends S3Task {

   /**
    * When defined, subdirectories are not included, which is the default.
    */
   @Input
   @Option(option = 'no-recursive',
           description = 'When defined, subdirectories are not included. Including subdirectories is the default.'
   )
   boolean noRecursive


   S3UploadSyncTask() {
      description = 'Synchronize a directory to an S3 bucket.'
      group = 'AWS'
   }

   @TaskAction
   def s3Sync() {
      MultipleFileUpload mfu = tm.uploadDirectory(bucketName, key, file, !noRecursive)
      mfu.waitForCompletion()
   }
}
