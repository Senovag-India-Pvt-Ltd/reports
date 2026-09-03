package com.sericulture;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.fonts.FontUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

@SpringBootApplication
public class SpringBootTemplateApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootTemplateApplication.class);

	public static void main(String[] args) {
		//Font font = new Font("Nirmala UI Semilight", Font.PLAIN, 12);
		//UIManager.put("Label.font", font);
		SpringApplication.run(SpringBootTemplateApplication.class, args);
	}

	@Bean
	public CommandLineRunner logRegisteredJasperFontsOnStartup() {
		return args -> {
			try {
				Collection<String> familyNames = FontUtil.getInstance(DefaultJasperReportsContext.getInstance())
						.getFontFamilyNames();
				logger.info("JasperReports registered font family count on startup: {}", familyNames.size());
				for (String name : familyNames) {
					logger.info("Registered JasperReports font family: '{}'", name);
				}
			} catch (Exception ex) {
				logger.error("Failed to eagerly load JasperReports font extensions on startup", ex);
			}
		};
	}

}
