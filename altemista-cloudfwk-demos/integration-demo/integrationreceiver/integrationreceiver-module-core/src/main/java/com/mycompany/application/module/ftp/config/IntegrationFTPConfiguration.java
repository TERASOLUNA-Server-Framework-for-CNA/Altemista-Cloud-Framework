package com.mycompany.application.module.ftp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationFTPConfiguration {

//    @Bean
//    public SessionFactory<FTPFile> ftpSessionFactory() {
//        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
//        sf.setHost("localhost");
//        sf.setPort(21);
//        sf.setUsername("ftpuser");
//        sf.setPassword("ftppassword");
//        sf.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE);
//        return new CachingSessionFactory<FTPFile>(sf);
//    }
    
//    @Bean
//    public FtpRemoteFileTemplate template() {
//        FtpRemoteFileTemplate template = new FtpRemoteFileTemplate(ftpSessionFactory());
//		template.setRemoteDirectoryExpression(new LiteralExpression("/integration/"));
//		template.setFileNameExpression(new LiteralExpression("*"));
//		return template;
//    }

//    @Bean
//    public FtpInboundFileSynchronizer ftpInboundFileSynchronizer() {
//        FtpInboundFileSynchronizer fileSynchronizer = new FtpInboundFileSynchronizer(ftpSessionFactory());
//        fileSynchronizer.setDeleteRemoteFiles(false);
//        fileSynchronizer.setRemoteDirectory("/integration/");
//        fileSynchronizer.setFilter(new FtpSimplePatternFileListFilter("*.txt"));
//        return fileSynchronizer;
//    }
//
//    @Bean
//    @InboundChannelAdapter(channel = "ftpChannel", poller = @Poller(fixedDelay = "5000"))
//    public MessageSource<File> ftpMessageSource() {
//        FtpInboundFileSynchronizingMessageSource source =
//                new FtpInboundFileSynchronizingMessageSource(ftpInboundFileSynchronizer());
//        source.setLocalDirectory(new File("/temp/ftp-inbound"));
//        source.setAutoCreateLocalDirectory(true);
//        source.setLocalFilter(new AcceptOnceFileListFilter<File>());
//        return source;
//    }
//    
//    
//
//    @Bean
//    @ServiceActivator(inputChannel = "ftpChannel")
//    public MessageHandler handler() {
//        return new MessageHandler() {
//
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//                System.out.println(message.getPayload());
//            }
//
//        };
//    }
    
}
