import com.github.gradle.node.npm.task.NpmTask

plugins {
  id("com.github.node-gradle.node") version "7.0.0"
}

val buildWeb = tasks.register<NpmTask>("buildWeb") {
  dependsOn(tasks.npmInstall)
  args.set(listOf("run", "build"))
}
