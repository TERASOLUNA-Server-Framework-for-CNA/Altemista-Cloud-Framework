curl https://altemista-gogs.helix.everis.cloud/Terasoluna/terasoluna-cicd/raw/master/jenkinsFiles/dev.jenkinsfile > dev.jenkinsfile
curl https://altemista-gogs.helix.everis.cloud/Terasoluna/terasoluna-cicd/raw/master/jenkinsFiles/int.jenkinsfile > int.jenkinsfile
curl https://altemista-gogs.helix.everis.cloud/Terasoluna/terasoluna-cicd/raw/master/jenkinsFiles/openshift.yaml > openshift.yaml

sed -i s/\${DEV_PROJECT}/$1/ *.jenkinsfile
sed -i s/\${STAGE_PROJECT}/$2/ *.jenkinsfile
sed -i s/\${PRO_PROJECT}/$3/ *.jenkinsfile
sed -i s/\${APP_AI}/$4/ *.jenkinsfile        

sed -i s/\${APP_AI}/$4/ *.yaml       
sed -i s/\${GOGS_USER}/$5/ *.yaml
sed -i s/\${CICD_ID}/$6/ *.yaml
sed -i s/\${WEBHOOK_SECRET}/$7/ *.yaml