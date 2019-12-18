package com.redpillanalytics.aws.containers

import groovy.util.logging.Slf4j

@Slf4j
class TaskGroupContainer {

   // Build Group defaults
   private static final String DEFAULT_GROUP = 'default'

   TaskGroupContainer(String name) {
      this.name = name
   }

   /**
    * The name of the container entity.
    */
   String name

   /**
    * The name of bucket to generate tasks for.
    */
   String bucket

   /**
    * The full name of the key in the bucket. Must be a directory and not a file.
    */
   String key

   /**
    * The full name of the file path locally. Must be a directory and not a file.
    */
   String path

   /**
    * Capture the debug status from the Gradle logging framework. Not currently used.
    */
   Boolean isDebugEnabled = log.isDebugEnabled()

   def getDomainName() {

      return ((getClass() =~ /\w+$/)[0] - "Container")
   }

   /**
    * Easy method for instrumentation configuring Gradle tasks.
    */
   def logTaskName(String task) {

      log.debug "${getDomainName()}: $name, TaskName: $task"

   }

   /**
    * This plugin has a default set of tasks that are configured with a single task group called 'default'. This method is used during configuration when special handling is needed for those tasks.
    */
   def isDefaultTask() {

      return (name == DEFAULT_GROUP) ? true : false

   }

   /**
    * A method that makes it easy for naming 'default' tasks versus non-'default' tasks.
    */
   def getTaskName(String baseTaskName) {

      // return either the baseTaskName or prepend with a name
      String taskName = isDefaultTask() ? baseTaskName : getName() + baseTaskName.capitalize()

      logTaskName(taskName)

      return taskName
   }

}
