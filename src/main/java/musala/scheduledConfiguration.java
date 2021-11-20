package musala;


import lombok.extern.slf4j.Slf4j;
import musala.service.DroneService;
import musala.service.impl.DroneServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Slf4j
@Configuration
@EnableScheduling
public class scheduledConfiguration {

    @Autowired
    DroneService droneService;

    @Scheduled(fixedDelay = 5000)
    public void executeTask1() {
        Long droneId =  droneService.getRecentDrone();
        if (droneId != 0){
            droneService.chekDroneBatteryStatus(droneId);
        }
    }
}
