package com.redpillanalytics.plugin

import groovy.util.logging.Slf4j
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll

@Slf4j
@Title("Execute :tasks task")
class PropertiesTest extends Specification {
   @Shared
   File projectDir

   @Shared
   String projectName = 'properties-test'

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
            |
            |""".stripMargin())
         file('settings.gradle', """rootProject.name = '$projectName'""")
      }
   }

   //helper task
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

   @Unroll
   def "properties contains #property"() {

      given:
      executeSingleTask('properties', ['-Paws.region=us-west-1','-S'])

      expect:
      result.output.contains("$property")

      where:
      property << ['aws.region','aws']
   }
}
