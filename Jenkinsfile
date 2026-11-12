pipeline {
   agent any
   tools {
       maven 'Maven'
       jdk 'Java'
   }
   environment {
       MAVEN_OPTS = "-Xmx512m -XX:MaxMetaspaceSize=256m"
   }
   stages {
       stage('Checkout') {
           steps {
               echo '📥 Getting code...'
               checkout scm
           }
       }
       stage('Build') {
           steps {
               echo '🔨 Building...'
               sh 'mvn clean compile'
           }
       }
       stage('Test') {
           steps {
               echo '🧪 Testing...'
               sh 'mvn test'
           }
       }
       stage('Package') {
           steps {
               echo '📦 Creating JAR...'
               sh 'mvn package -DskipTests'
           }
       }
   }
}