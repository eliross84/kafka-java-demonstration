package edu.nwmissouri.demonstratekj.simple;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class Producer {
  static int answer=0;
  private static Scanner in;

  public static void main(String[] argv) throws Exception {
    if (argv.length != 1) {
      System.err.println("Please specify 1 parameter (the name of the topic)");
      System.exit(-1);
    }
    String topicName = argv[0];
    in = new Scanner(System.in);
    System.out.println("Thank you for providing the topic " + topicName + "\n");
    System.out.println("Enter message (type exit to quit).\n");

    // Configure the Producer
    Properties configProperties = new Properties();
    configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.ByteArraySerializer");
    configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    System.out.println("The configuration properties are: " + configProperties.toString());
    System.out.println("\nWill use this configuration to create a producer.\n");

    org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(configProperties);

    String message = "Hello, and welcome to the Kafka Math Quiz";
    ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName, message);
    producer.send(rec);

    String line = in.nextLine();
    while (!line.equals("exit")) {
      Random rand = new Random();
      int random = rand.nextInt(3);
      message = createMessage(random);
      rec = new ProducerRecord<String, String>(topicName,message);
      producer.send(rec);
      line = in.nextLine();
      int userAnswer = Integer.parseInt(line);
      if(userAnswer == answer){
        message = "You are correct! :)";
      }
      else{
        message = "You are wrong... :(";
      }
      rec = new ProducerRecord<String, String>(topicName,message);
      producer.send(rec);
    }
      in.close();
      producer.close();
  }
  
  private static String createMessage(int i) {
    String[] questions = { "What is 2+2?", "What is 12*12?", "What is the square root of 121?" };
      String result= questions[i];

      if(i == 0){
        answer = 4;
      }
      else if(i == 1){
        answer = 144;
      }
      else if(i == 2){
        answer = 11;
      }
   
    return result;
  }

 
}


