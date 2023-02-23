# Serverless with Knative Eventing and Quarkus [![Twitter](https://img.shields.io/twitter/follow/piotr_minkowski.svg?style=social&logo=twitter&label=Follow%20Me)](https://twitter.com/piotr_minkowski)

[![CircleCI](https://circleci.com/gh/piomin/sample-quarkus-serverless-kafka.svg?style=svg)](https://circleci.com/gh/piomin/sample-quarkus-serverless-kafka)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=piomin_sample-quarkus-serverless-kafka)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=piomin_sample-quarkus-serverless-kafka&metric=bugs)](https://sonarcloud.io/dashboard?id=piomin_sample-quarkus-serverless-kafka)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=piomin_sample-quarkus-serverless-kafka&metric=coverage)](https://sonarcloud.io/dashboard?id=piomin_sample-quarkus-serverless-kafka)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=piomin_sample-quarkus-serverless-kafka&metric=ncloc)](https://sonarcloud.io/dashboard?id=piomin_sample-quarkus-serverless-kafka)

## Getting Started
Currently, you may find here some examples of microservices implementation using different projects from Quarkus. All the examples are divided into the branches and described in a separated articles on my blog. Here's a full list of available examples:
1. Using Knative Serving and Eventing components with Quarkus and Kafka to build event-driven microservices architecture in declarative way. The example is available in the branch [master](https://github.com/piomin/sample-quarkus-serverless-kafka/tree/master). A detailed guide may be find in the following article: [Knative Eventing with Kafka and Quarkus](https://piotrminkowski.com/2021/03/31/knative-eventing-with-kafka-and-quarkus/)

## Usage

## Architecture

## Test on OpenShift

1. Login to OpenShift Dashboard https://console-openshift-console.apps.qyt1tahi.eastus.aroapp.io/
2. Create your project
3. Create Knative `Broker`

```yaml
apiVersion: eventing.knative.dev/v1
kind: Broker
metadata:
  annotations:
    eventing.knative.dev/broker.class: MTChannelBasedBroker
  name: default
spec:
  config:
    apiVersion: v1
    kind: ConfigMap
    name: config-br-default-channel
    namespace: knative-eventing
```

4. Deploy applications

`order-service` -> quay.io/pminkows/order-service. Set env `KAFKA_TOPIC`, `TICK_TIMEOUT` \
`stock-service` -> quay.io/pminkows/stock-service. Set env `KAFKA_TOPIC` \
`payment-service` -> quay.io/pminkows/payment-service. Set env `KAFKA_TOPIC`

5. Create `KafkaBinding` for each application to inject Kafka address

For `stock-service`:
```yaml
apiVersion: bindings.knative.dev/v1beta1
kind: KafkaBinding
metadata:
  name: kafka-binding-stock
spec:
  subject:
    apiVersion: serving.knative.dev/v1
    kind: Service
    name: stock-service
  bootstrapServers:
    - my-cluster-kafka-bootstrap.kafka:9092
```
For `payment-service`:
```yaml
apiVersion: bindings.knative.dev/v1beta1
kind: KafkaBinding
metadata:
  name: kafka-binding-payment
spec:
  subject:
    apiVersion: serving.knative.dev/v1
    kind: Service
    name: payment-service
  bootstrapServers:
    - my-cluster-kafka-bootstrap.kafka:9092
```
For `order-service`:
```yaml
apiVersion: bindings.knative.dev/v1beta1
kind: KafkaBinding
metadata:
  name: kafka-binding-order
spec:
  subject:
    apiVersion: serving.knative.dev/v1
    kind: Service
    name: order-service
  bootstrapServers:
    - my-cluster-kafka-bootstrap.kafka:9092
```

6. Create `KafkaSource` to get messages from Kafka and send them to the Knative `Broker`

```yaml
apiVersion: sources.knative.dev/v1beta1
kind: KafkaSource
metadata:
  name: kafka-source-to-broker
spec:
  bootstrapServers:
    - my-cluster-kafka-bootstrap.kafka:9092
  topics:
    - <your-order-events-topic>
    - <your-reserve-events-topic>
  sink:
    ref:
      apiVersion: eventing.knative.dev/v1
      kind: Broker
      name: default
```

7. Create Knative `Trigger` for applications

For `stock-service`:
```yaml
apiVersion: eventing.knative.dev/v1
kind: Trigger
metadata:
  name: stock-trigger
spec:
  broker: default
  filter:
    attributes:
      source: /apis/v1/namespaces/<your-namespace>/kafkasources/kafka-source-to-broker#<your-topic>
      type: dev.knative.kafka.event
  subscriber:
    ref:
      apiVersion: serving.knative.dev/v1
      kind: Service
      name: stock-service
    uri: /reserve
```

For `payment-service`:
```yaml
apiVersion: eventing.knative.dev/v1
kind: Trigger
metadata:
  name: payment-trigger
spec:
  broker: default
  filter:
    attributes:
      source: /apis/v1/namespaces/<your-namespace>/kafkasources/kafka-source-to-broker#<your-topic>
      type: dev.knative.kafka.event
  subscriber:
    ref:
      apiVersion: serving.knative.dev/v1
      kind: Service
      name: payment-service
    uri: /reserve
```

For `order-service`:
```yaml
apiVersion: eventing.knative.dev/v1
kind: Trigger
metadata:
  name: order-trigger
spec:
  broker: default
  filter:
    attributes:
      source: /apis/v1/namespaces/<your-namespace>/kafkasources/kafka-source-to-broker#<your-topic>
      type: dev.knative.kafka.event
  subscriber:
    ref:
      apiVersion: serving.knative.dev/v1
      kind: Service
      name: order-service
    uri: /confirm
```

8. Configure autoscaling for `payment-service` and `stock-service`

Edit Knative `Service` YAML and add the following annotations:
```yaml
      annotations:
        autoscaling.knative.dev/target: "50"
        autoscaling.knative.dev/metric: "rps"
```

9. Change timeout for `order-service`

Edit Knative `Service` YAML and change `TICK_TIMEOUT` env