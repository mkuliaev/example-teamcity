import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.versionedSettings
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {
    description = "Contains all other projects"

    features {
        buildReportTab {
            id = "PROJECT_EXT_1"
            title = "Code Coverage"
            startPage = "coverage.zip!index.html"
        }
    }

    cleanup {
        baseRule {
            preventDependencyCleanup = false
        }
    }

    subProject(ExampleTeamcity)
}


object ExampleTeamcity : Project({
    name = "Example Teamcity"

    vcsRoot(ExampleTeamcity_HttpsGithubComAndreyGrozovExampleTeamcityGitRefsHeadsMaster)
    vcsRoot(ExampleTeamcity_HttpsGithubComAragastmatbExampleTeamcityGitRefsHeadsMaster)
    vcsRoot(ExampleTeamcity_HttpsGithubComAndreyGrozovExampleTeamcityGitRefsHeadsMaster1)

    buildType(ExampleTeamcity_TestOrDeployToNexus)
    buildType(ExampleTeamcity_Testing)

    features {
        versionedSettings {
            id = "PROJECT_EXT_1"
        }
    }
})

object ExampleTeamcity_TestOrDeployToNexus : BuildType({
    name = "Test or Deploy to Nexus"

    artifactRules = "+:target/*.jar"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            name = "Deploy"

            conditions {
                contains("teamcity.build.branch", "master")
            }
            goals = "clean deploy"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            userSettingsSelection = "settings.xml"
        }
        maven {
            name = "Test only"

            conditions {
                doesNotContain("teamcity.build.branch", "master")
            }
            goals = "clean test"
        }
    }

    triggers {
        vcs {
        }
    }
})

object ExampleTeamcity_Testing : BuildType({
    name = "Testing"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }
})

object ExampleTeamcity_HttpsGithubComAndreyGrozovExampleTeamcityGitRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/andrey-grozov/example-teamcity.git#refs/heads/master"
    url = "https://github.com/andrey-grozov/example-teamcity.git"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "a.grozov@yandex.ru"
        password = "credentialsJSON:bce13d45-9839-4ec3-ab37-a18f8d5e91fe"
    }
})

object ExampleTeamcity_HttpsGithubComAndreyGrozovExampleTeamcityGitRefsHeadsMaster1 : GitVcsRoot({
    name = "https://github.com/andrey-grozov/example-teamcity.git#refs/heads/master (1)"
    url = "https://github.com/andrey-grozov/example-teamcity.git"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "a.grozov@yandex.ru"
        password = "credentialsJSON:bce13d45-9839-4ec3-ab37-a18f8d5e91fe"
    }
})

object ExampleTeamcity_HttpsGithubComAragastmatbExampleTeamcityGitRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/aragastmatb/example-teamcity.git#refs/heads/master"
    url = "https://github.com/aragastmatb/example-teamcity.git"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "aragast"
        password = "credentialsJSON:0df58bae-9b65-4de0-bb4a-f24d94cb9562"
    }
})
