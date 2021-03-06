package com.redpillanalytics.aws.tasks

import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import groovy.util.logging.Slf4j
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@Slf4j
class S3DownloadTask extends S3Task {

   S3DownloadTask() {
      description = 'Download a file from S3.'
      group = 'AWS'
      outputs.upToDateWhen { false }
   }

   @OutputFile
   File getOutputFile() {
      return file
   }

   @Internal
   S3Object getObject() {
      return defaultClient.getObject(bucketName, keyName)
   }

   @TaskAction
   def s3Download() {

      try {
         outputFile.delete()
      } catch (Exception e) {
         log.warn "${e.message}"
      }

      S3ObjectInputStream s3is = object.getObjectContent()
      outputFile.append(s3is)

   }
}
