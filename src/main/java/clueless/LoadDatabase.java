package clueless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(GameRepository gameRepository) {

    return args -> {
      log.info("Preloading " + gameRepository.save(new Game(new Player("Zach Bialik", (long) 1))));
      log.info("Preloading " + gameRepository.save(new Game(new Player("Megan Tucker", (long) 1))));
      log.info("Preloading " + gameRepository.save(new Game(new Player("Alex Wang", (long) 1))));
    };
  }
}