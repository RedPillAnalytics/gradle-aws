package com.redpillanalytics.aws

import groovy.util.logging.Slf4j
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Title

@Slf4j
@Stepwise
@Title("Execute ODI export tasks")
class S3UploadTest extends Specification {

   @Shared
   File projectDir

   @Shared
   String projectName = 's3-upload', bucket = 'rpa-s3-test', taskName

   @Shared
   FileTreeBuilder projectTree

   @Shared
   BuildResult result

   def setupSpec() {

      projectTree = new FileTreeBuilder(new File(System.getProperty("projectBase")))
      projectDir = projectTree.dir(projectName)
      projectDir.deleteDir()

      projectTree.dir(projectName) {
         file('build.gradle', """
            |plugins {
            |   id 'com.redpillanalytics.gradle-aws'
            |   id "com.redpillanalytics.gradle-analytics" version "1.2.3"
            |}
            |aws {
            | s3 {
            |   test {
            |     bucket = '${bucket}'
            |     key = 'build'
            |     path = 'build'
            |   }
            |  }
            |}
            |
            |""".stripMargin())
         file('settings.gradle', """rootProject.name = '$projectName'""")
      }
   }

   // helper method
   def executeSingleTask(String taskName, List otherArgs = []) {

      otherArgs.add(0, taskName)

      log.warn "runner arguments: ${otherArgs.toString()}"

      // execute the Gradle test build
      result = GradleRunner.create()
              .withProjectDir(projectDir)
              .withArguments(otherArgs)
              .withPluginClasspath()
              .forwardOutput()
              .build()
   }

   def "Execute :s3Upload task with defaults"() {
      given:
      taskName = 's3Upload'
      result = executeSingleTask(taskName, ['-Si', '--bucket-name', bucket, '--file-path', 'settings.gradle'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }

   def "Execute :s3Upload task with custom key-name"() {
      given:
      taskName = 's3Upload'
      result = executeSingleTask(taskName, ['-Si', '--bucket-name', bucket, '--file-path', 'settings.gradle', '--key-name', 'custom-key-name.txt'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }

   def "Execute :s3UploadSync task with defaults"() {
      given:
      taskName = 's3UploadSync'
      result = executeSingleTask(taskName, ['-Si', '--bucket-name', bucket, '--file-path', 'build'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }

   def "Execute :s3UploadSync task with custom key-name"() {
      given:
      taskName = 's3UploadSync'
      result = executeSingleTask(taskName, ['-Si', '--bucket-name', bucket, '--file-path', 'build', '--key-name', 'custom-build'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }
   def "Execute :testS3UploadSync task with defaults"() {
      given:
      taskName = 'testS3UploadSync'
      result = executeSingleTask(taskName, ['-Si'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }
}