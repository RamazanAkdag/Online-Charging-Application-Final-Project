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
                sh '''
                    SONAR_TOKEN=$(cat /opt/secrets/sonarqube_secret)
                    SONAR_HOST=$(cat /opt/secrets/sonarqube_ip)

                    $SCANNER_HOME/bin/sonar-scanner \
                        -Dsonar.projectName=OCS \
                        -Dsonar.projectKey=OCS \
                        -Dsonar.java.binaries=. \
                        -Dsonar.token=$SONAR_TOKEN \
                        -Dsonar.host.url=http://$SONAR_HOST:9000 \
                    | tee sonar-log.txt
                '''
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    sleep 5 // SonarQube sunucusunun analizi işlemesi için ilk bekleme

                    echo "📄 Sonar Log İçeriği:"
                    def sonarLog = readFile('sonar-log.txt')
                    echo sonarLog

                    // 🔍 taskId'yi logdan çek
                    def taskId = sh(
                        script: "grep -o 'task?id=[^\" ]*' sonar-log.txt | cut -d'=' -f2",
                        returnStdout: true
                    ).trim()

                    if (!taskId) {
                        error("❌ taskId bulunamadı! 'sonar-log.txt' içinde 'task?id=' satırı yok.")
                    }

                    // 🔐 Secret'ları al
                    def sonarToken = sh(script: "cat /opt/secrets/sonarqube_secret", returnStdout: true).trim()
                    def sonarHost = sh(script: "cat /opt/secrets/sonarqube_ip", returnStdout: true).trim()

                    // 🔄 analysisId için retry mekanizması
                    def analysisId = ''
                    def retries = 10

                    for (int i = 0; i < retries; i++) {
                        analysisId = sh(
                            script: "curl -s -u ${sonarToken}: http://${sonarHost}:9000/api/ce/task?id=${taskId} | jq -r '.task.analysisId'",
                            returnStdout: true
                        ).trim()

                        if (analysisId && analysisId != "null") {
                            echo "✅ Analysis ID bulundu: ${analysisId}"
                            break
                        }

                        echo "⏳ Analysis ID henüz hazır değil, 5 saniye bekleniyor... (Deneme ${i+1}/${retries})"
                        sleep 5
                    }

                    if (!analysisId || analysisId == "null") {
                        error("❌ Analysis ID alınamadı, analiz tamamlanmamış olabilir.")
                    }

                    // ✅ Quality Gate kontrolü
                    def gateStatus = sh(
                        script: "curl -s -u ${sonarToken}: http://${sonarHost}:9000/api/qualitygates/project_status?analysisId=${analysisId} | jq -r '.projectStatus.status'",
                        returnStdout: true
                    ).trim()

                    echo "✅ Quality Gate sonucu: ${gateStatus}"

                    if (gateStatus != "OK") {
                        error("❌ Quality Gate başarısız: ${gateStatus}")
                    }
                }
            }
        }


        stage('Parse Nexus IP and Deploy to Nexus') {
            steps {
                script {
                    def nexusIp = sh(
                        script: "cat /opt/secrets/nexus_ip",
                        returnStdout: true
                    ).trim()
                    echo "📡 Nexus IP: ${nexusIp}"

                    sh """
                        mvn clean deploy \\
                        -DaltDeploymentRepository=nexus::default::http://${nexusIp}:8081/repository/maven-releases/
                    """
                }
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
