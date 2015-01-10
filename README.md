# Couchbase-Kafka Integration Demo

## Environment

Vagrant can help to prepare Couchbase and Kafka node.

Install prerequisites: [Vagrant](https://www.vagrantup.com/downloads.html), [Ansible](http://docs.ansible.com/intro_installation.html) and [vagrant-hostupdater](https://github.com/cogitatio/vagrant-hostsupdater).

    yum install https://dl.bintray.com/mitchellh/vagrant/vagrant_1.7.2_x86_64.rpm
    yum install ansible
    vagrant plugin install vagrant-hostsupdater

And now power on the cluster

    cd env
    vagrant up

Now you have two nodes accessible using addresses `couchbase1.vagrant`
and `kafka1.vagrant`.

## Build

Compile jar file:

    ./gradlew shadowJar

Execute demo application, which listens all changes in default bucket
and posts corresponding messages in kafka.

    java -cp build/libs/kafka-couchbase-demo-1.0-all.jar example.BasicExample
