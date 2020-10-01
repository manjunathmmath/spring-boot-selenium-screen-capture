package com.boilerplate.controller;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import java.util.*;

@Controller
public class HomeController {
    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping(value = {"/",})
    public String index(Model model) {
        return "home/index";
    }

    @GetMapping(path = {"/getScreenShot"})
    @ResponseBody
    public Map<String, String> getScreenShot(Model model, @RequestParam Map<String, String> reqParam) throws Exception {
        Map<String, String> data = new HashMap<>();
        boolean HEADLESS = false;

        Path firefox = Paths.get("src/main","resources/geckodriver","geckodriver.exe");
        firefox.toFile().setExecutable(true);
        System.setProperty("webdriver.gecko.driver", firefox.toAbsolutePath().toString());
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.SEVERE);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setAcceptInsecureCerts(true);

        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
        FirefoxOptions options = new FirefoxOptions().merge(capabilities);

        if (HEADLESS) {
            options.addArguments("--headless");
        }
        options.addArguments("--proxy-server='direct://'");
        options.addArguments("--proxy-bypass-list=*");
        options.addArguments("--proxy-server=");
        WebDriver driver = new FirefoxDriver(options);
        WebElement ele;
        driver.get(reqParam.get("url"));
        driver.manage().window().maximize();
        Screenshot screenshot = null;
        Float aspectValue = 1.3F;
        String filename = "";
        Calendar startCal = Calendar.getInstance();
        filename = String.valueOf(startCal.getTimeInMillis());
        File src = null;
        String filePath ="c://tmp";

        screenshot=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(aspectValue),1000)).takeScreenshot(driver);
        System.out.println(screenshot);
        try {
            if (filename != "") {
                filename = filename.replaceAll("\\s+", "-").replaceAll("\\*", "-");
            }
            ImageIO.write(screenshot.getImage(),"PNG",new File(filePath + "/" + filename + ".png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            driver.quit();
        }
        driver.quit();
        data.put("status","success");
        data.put("filename",filename);
        return data;
    }
}
