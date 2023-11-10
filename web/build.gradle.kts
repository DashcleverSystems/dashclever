import com.github.gradle.node.npm.task.NpmTask

plugins {
  id("com.github.node-gradle.node") version "7.0.1"
}

node {
  download.set(true)
}

val build = tasks.register<NpmTask>("build") {
  dependsOn(tasks.npmInstall)
  args.set(listOf("run", "build"))
}

val start = tasks.register<NpmTask>("start") {
  dependsOn(tasks.npmInstall)
  args.set(listOf("run", "start"))
}

val generateHttpClients = tasks.register<NpmTask>("generateHttpClients") {
  args.set(listOf("run", "generate:api"))
}

val cleanHttpClients = tasks.register<NpmTask>("cleanHttpClients") {
  args.set(listOf("run", "pregenerate:api"))
}
