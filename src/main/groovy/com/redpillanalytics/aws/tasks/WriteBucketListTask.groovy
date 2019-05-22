package com.redpillanalytics.aws.tasks

import groovy.util.logging.Slf4j
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@Slf4j
class WriteBucketListTask extends S3Task {

   WriteBucketListTask() {
      description = 'Write a file listing all objects in the bucket.'
      group = 'AWS'
   }

   @OutputFile
   File getBucketList() {
      return project.file("${project.buildDir}/gradle-aws/${bucketName}.json")
   }

   @TaskAction
   def s3Download() {
      writeObjectList(bucketName)
   }
}
