
package amqsget;
import java.io.IOException;

import com.ibm.mq.*;
import com.ibm.mq.headers.*;

public class Message {

      /**
      * @param args
      * @throws MQException 
       * @throws IOException 
       * @throws MQDataException 
       */
      public static void main(String[] args)   {
            MQQueueManager qManager=null;
            MQQueue queue=null;
            if (args.length<5){
                  System.out.println("Usage Required <Host> <port> <channel> <Qmanager> <Queue>");
                  System.exit(1);
            }
            try{
            MQEnvironment mqenv= new com.ibm.mq.MQEnvironment();
            
            
            mqenv.hostname = args[0]; 
            mqenv.port = Integer.parseInt(args[1]); 
            mqenv.channel = args[2]; 
            mqenv.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES); //Set TCP/IP or server Connection
             qManager=new MQQueueManager(args[3]);
             queue=qManager.accessQueue(args[4], MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE );
            MQGetMessageOptions getOptions = new MQGetMessageOptions();
            getOptions.options = MQC.MQGMO_NO_WAIT + MQC.MQGMO_FAIL_IF_QUIESCING + MQC.MQGMO_CONVERT ;
            
           while(queue.getCurrentDepth()!=0){
              MQMessage message=new MQMessage();
              new MQHeaderIterator (message).skipHeaders();
            queue.get(message, getOptions);
            byte[] b = new byte[message.getDataLength()];
            message.readFully(b);
            System.out.println(new String(b));
            
            }
            }
            catch(MQException e){
                  System.out.println("MQOPEN ended with reason code "+e.reasonCode);
                  
            }
            catch(Exception e){
                  System.out.println(e);
                  System.exit(1);
            }
            finally{
                  
                        try {
                              if(qManager!=null&&qManager.isOpen)
                              qManager.close();
                              if(queue!=null&&queue.isOpen())
                                    queue.close();
                        } catch (MQException e) {
                              // TODO Auto-generated catch block
                              //e.printStackTrace();
                        }
                  
                        
            }
           
            
      }

}
