
download strimizi 
https://github.com/strimzi/strimzi-kafka-operator/releases/tag/0.13.0

enable admin permissions:

kubectl create clusterrolebinding cluster-admin-binding \
  --clusterrole=cluster-admin \
  --user=$(gcloud config get-value core/account)


set the name space for the yaml files:

sed -i '' 's/namespace: .*/namespace: default/' install/cluster-operator/*RoleBinding*.yaml

install the operator:

kubectl apply -f install/cluster-operator -n default

setup kafka and a kafka topic

kubectl apply -f examples/kafka/kafka-ephemeral.yaml
kubectl apply -f examples/topic/kafka-topic.yaml

Test producer:
kubectl run kafka-producer -ti --image=strimzi/kafka:0.13.0-kafka-2.3.0 --rm=true --restart=Never -- bin/kafka-console-producer.sh --broker-list my-cluster-kafka-bootstrap:9092 --topic my-topic

Test consumer:
kubectl run kafka-consumer -ti --image=strimzi/kafka:0.13.0-kafka-2.3.0 --rm=true --restart=Never -- bin/kafka-console-consumer.sh --bootstrap-server my-cluster-kafka-bootstrap:9092 --topic my-topic --from-beginning

Build the docker image and deploy it:
sbt docker:publishLocal

gcloud auth configure-docker
docker tag producer:0.3 gcr.io/none-219021/producer:0.3
gcloud docker -- push gcr.io/none-219021/producer:0.3
gcloud container images list