Openshift Templates for deploying a ACF App on Altemista (Spain)

For use in generic Openshift follow the next steps:

1.- Have an management-infra proyect, with permisions to create new projects on OS
2.- Have or install the basic dependencies
	Images avaiable:
		jenkins:v3.7
		openshiftdemos/gogs:0.11.29 (uses OpenShiftDemos template)
		openshift/origin:v3.6.0
		openshiftdemos/sonarqube:6.7
		openshift/jenkins-slave-maven-nodejs-kube-rhel7:latest
	Templates avaiable:
		gogs-persistent-template.yaml
		openshift-sonarqube-embedded-template (see altemista-project-template)
	Connection to:
		https://raw.githubusercontent.com/OpenShiftDemos/
		Any git (Gogs) where to put custom templates
3.- Create a project under the gogs service for store all the files and directories under this folder
4.- Modify all the yaml to use the url of the Gogs service
5.- Upload all the files
6.- Install the template, may use the bat or the "oc command"
