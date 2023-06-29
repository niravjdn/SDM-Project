node {

  environment {
        MAVEN_OPTS = '-Xmx2g'
  }
  
  stage("Clone the project") {
    git branch: 'H2-DB', url: 'https://github.com/niravjdn/SDM-Project.git'
  }

  stage("Compilation") {
    dir('Vehicle Rental System') {
      sh "./mvnw clean install -DskipTests"
    }
  }

  stage("Tests and Deployment") {
    stage("Running unit tests") {
      dir('Vehicle Rental System') {
        sh "./mvnw test -Punit"
      }
    }
    stage("Deployment") {
      dir('Vehicle Rental System') {
        sh 'nohup ./mvnw spring-boot:run &'
      }
    }
  }
}
