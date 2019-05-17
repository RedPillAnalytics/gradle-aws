package com.redpillanalytics.aws.tasks

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.options.Option

@Slf4j
class S3Task extends DefaultTask {

   @Internal
   AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient()

   @Internal
   TransferManager tm = TransferManagerBuilder.standard().build()

   /**
    * The bucket to interact with.
    */
   @Input
   @Option(option = "bucket-name",
           description = "The bucket to interact with.")
   String bucketName

   /**
    * The key name to interact with.
    */
   @Input
   @Optional
   @Option(option = "key-name",
           description = "The key name to interact with.")
   String keyName

   /**
    * The local file path to interact with.
    */
   @Input
   @Optional
   @Option(option = "file-path",
           description = "The local file path to interact with.")
   String filePath

   @Internal
   getFile() {
      File file = filePath ? project.file(filePath) : project.file(keyName)
      log.debug "File: $file"
      return file
   }

   @Internal
   getBucketName(){
      log.info "Bucket: $bucketName"
      return bucketName
   }

   @Input
   getKey() {
      String key = keyName ?: file.name
      log.info "Key: $key"
      return key
   }
}
