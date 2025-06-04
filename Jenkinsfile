pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    environment {
        SCANNER_HOME = tool 'sonar-scanner'
    }

    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'ansible', url: 'https://github.com/RamazanAkdag/Online-Charging-Application-Final-Project'
            }
        }

        stage('Compile') {
            steps {
                withMaven(globalMavenSettingsConfig: '', jdk: 'jdk17', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
                    sh "mvn compile -DskipTests"
                }
            }
        }

    //    stage('Test') {
    //      steps{
    //              withMaven(globalMavenSettingsConfig: '', jdk: 'jdk17', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
    //                    sh "mvn test"
     //             }  
       //     }
      //  }

        stage('File System Scan') {
            steps {
                sh "trivy fs --format table -o trivy-fs-report.html ."
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        $SCANNER_HOME/bin/sonar-scanner \
                        -Dsonar.projectName=OCS \
                        -Dsonar.projectKey=OCS \
                        -Dsonar.java.binaries=.
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonar-token'
                }
            }
        }

        stage('Parse Nexus IP') {
            steps {
                script {
                    env.NEXUS_IP = sh(
                        script: "grep '^nexus=' ansible/ec2_ids | cut -d'=' -f2",
                        returnStdout: true
                    ).trim()
                    echo "📡 Nexus IP: ${env.NEXUS_IP}"
                }
            }
        }

        stage('Build and Deploy to Nexus') {
            steps {
                sh """
                    mvn clean deploy \
                    -DaltDeploymentRepository=nexus::default::http://${NEXUS_IP}:8081/repository/maven-releases/
                """
            }
        }
        
        stage('Set Image List') {
            steps {
                script {
                    imageList = [
                        'account-order-management',
                        'account-balance-management-function',
                        'charging-gateway-function',
                        'diameter-gateway',
                        'notification-function',
                        'online-charging-system',
                        'traffic-generator-function'
                    ]
                }
            }
        }

        stage('Dockerize with Jib') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        imageList.each { imageName ->
                            sh """
                                mvn -pl ${imageName} jib:dockerBuild \
                                -Ddocker.username=$DOCKER_USER \
                                -Ddocker.password=$DOCKER_PASS
                            """
                        }
                    }
                }
            }
        }

        stage('Docker Image Scan') {
            steps {
                script {
                    imageList.each { imageName ->
                        def fullImageName = "beko2001/${imageName}"
                        def reportName = "trivy-scan-${imageName.replaceAll('-', '_')}.html"
                        sh "trivy image --format table -o ${reportName} ${fullImageName}:latest"
                    }
                }
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        imageList.each { imageName ->
                            def fullImageName = "beko2001/${imageName}"
                            sh """
                                echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                                docker push ${fullImageName}:latest
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy To Kubernetes') {
            steps {
                echo 'Deploying Kubernetes manifests...'
                sh '''
                    kubectl apply -f kubernetes/services/
                    kubectl apply -f kubernetes/deployments/
                '''
                echo 'Deployment completed.'
            }
        }

    }

    post {
        always {
            archiveArtifacts artifacts: 'trivy-scan-*.html', allowEmptyArchive: true
        }
    }
}
