pipeline { 
    agent any  
    tools {
        maven 'M3'
    }
    stages { 
        stage('Build') { 
            steps {
               echo 'This is a minimal pipeline.'
               sh ' mvn compile '
            }
        }
    }
}
