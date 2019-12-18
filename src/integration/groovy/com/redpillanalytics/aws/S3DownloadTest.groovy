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
class S3DownloadTest extends Specification {

   @Shared
   File projectDir

   @Shared
   String projectName = 's3-download', bucket = 'rpa-s3-test', taskName

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
            |}
            |aws {
            | s3 {
            |   test {
            |     bucket = '${bucket}'
            |     key = 'custom-build'
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

   def "Execute :s3Download task with defaults"() {
      given:
      taskName = 's3Download'
      result = executeSingleTask(taskName, ['-Si', '--bucket-name', bucket, '--key-name', 'test-file.txt'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }

   def "Execute :s3Download task with custom file"() {
      given:
      taskName = 's3Download'
      result = executeSingleTask(taskName, ['-Si', '--bucket-name', bucket, '--key-name', 'test-file.txt', '--file-path', 'custom-file-path.txt'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }

   def "Execute :s3Download task with custom file path"() {
      given:
      taskName = 's3Download'
      result = executeSingleTask(taskName, ['-Si', '--bucket-name', bucket, '--key-name', 'test-file.txt', '--file-path', 'custom-dir/custom-file-path.txt'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }

   def "Execute :s3DownloadSync task with defaults"() {
      given:
      taskName = 's3DownloadSync'
      result = executeSingleTask(taskName, ['-Si', '--bucket-name', bucket, '--key-name', 'custom-build'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }

   def "Execute :s3DownloadSync task with custom file path"() {
      given:
      taskName = 's3DownloadSync'
      result = executeSingleTask(taskName, ['-Si', '--bucket-name', bucket, '--key-name', 'custom-build', '--file-path', 'custom-build-sync-dir'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }

   def "Execute :testS3DownloadSync task with defaults"() {
      given:
      taskName = 'testS3DownloadSync'
      result = executeSingleTask(taskName, ['-Si'])

      expect:
      result.task(":${taskName}").outcome.name() != 'FAILED'
   }
}