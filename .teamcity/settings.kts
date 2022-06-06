import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubConnection
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

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

version = "2022.04"

project {
    description = "Contains all other projects"

    features {
        buildReportTab {
            id = "PROJECT_EXT_1"
            title = "Code Coverage"
            startPage = "coverage.zip!index.html"
        }
        githubConnection {
            id = "PROJECT_EXT_5"
            displayName = "GitHub.com"
            clientId = "kokobops"
            clientSecret = "credentialsJSON:2cdd3386-fa64-4a04-913a-2a68fa7f653e"
        }
    }

    cleanup {
        baseRule {
            preventDependencyCleanup = false
        }
    }

    subProject(Kokobops)
}


object Kokobops : Project({
    name = "kokobops"

    vcsRoot(Kokobops_GitGithubComKokobopsExampleTeamcityGit)
})

object Kokobops_GitGithubComKokobopsExampleTeamcityGit : GitVcsRoot({
    name = "git@github.com:kokobops/example-teamcity.git"
    url = "git@github.com:kokobops/example-teamcity.git"
    pushUrl = "git@github.com:kokobops/example-teamcity.git"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = customPrivateKey {
        customKeyPath = "/opt/teamcity/.ssh/id_rsa"
        passphrase = "credentialsJSON:9cd0c03c-00c3-4651-833b-9345be8e4b45"
    }
})
