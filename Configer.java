package com.artemis.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.artemis.module.Module;
import com.artemis.Artemis;
import com.artemis.altmanager.AltLoginThread;
import com.artemis.command.friend.sub.Friend;
import com.artemis.logger.Logger;
import com.artemis.logger.Logger.LogType;
import com.artemis.settings.Setting;

import net.minecraft.client.Minecraft;


public class Configer {
	

	public File dir;
	public File dataFile;
	public File saveFile;
	public File removeFile;
	public ArrayList<File> configs = new ArrayList<File>();
	public static Configer instance = new Configer();
    private AltLoginThread thread;
	
	public ArrayList<File> getConfigs() {
		return this.configs;
	}
    
	public void Config(String filename) {
		dir = new File(Minecraft.getMinecraft().mcDataDir, "Artemis Configs");
		if (!dir.exists()) {
			dir.mkdir();
			Artemis.addChatMessage("Folder does not exist! Created new folder.");
			Logger.instance.log("Folder does not exist! Created new folder.", LogType.ERROR);
			return;
		}
		dataFile = new File(dir, filename + ".txt");
		
		if (!dataFile.exists()) {
			Artemis.addChatMessage("File does not exist!");
			Logger.instance.log("File does not exist!", LogType.ERROR);
			return;
		}
        this.load();
	
	}
	
	public void configSave(String filename) {
		dir = new File(Minecraft.getMinecraft().mcDataDir, "Artemis Configs");
		if (!dir.exists()) {
			dir.mkdir();
			Artemis.addChatMessage("Folder does not exist! Created new folder.");
			Logger.instance.log("Folder does not exist! Created new folder.", LogType.ERROR);
			return;
		}
		saveFile = new File(dir, filename + ".txt");
		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}else {
			Artemis.addChatMessage("File already exists!");
			Logger.instance.log("File already exists!", LogType.ERROR);
			return;
		}
		this.configs.add(saveFile);
        this.save();
	
	}
	
	public void configRemove(String filename) {
		dir = new File(Minecraft.getMinecraft().mcDataDir, "Artemis Configs");
		if (!dir.exists()) {
			dir.mkdir();
			Artemis.addChatMessage("Folder does not exist! Created new folder.");
			Logger.instance.log("Folder does not exist! Created new folder.", LogType.ERROR);
			return;
		}
		removeFile = new File(dir, filename + ".txt");
		if (removeFile.exists()) {
			try {
				removeFile.delete();
			} catch (Exception e) {e.printStackTrace();}
		}else {
			Artemis.addChatMessage("File does not exist!");
			Logger.instance.log("File does not exist!", LogType.ERROR);
		}
	
	}
	 public void save() {
		    ArrayList<String> toSave = new ArrayList<>();
		    for (com.artemis.module.Module mod : Artemis.instance.moduleManager.getModules())
		      toSave.add("MOD:" + mod.getHudname() + ":" + mod.isToggled() + ":" + mod.getKey()); 
		    for (Setting set : Artemis.instance.settingsManager.getSettings()) {
		      if (set.isCheck())
		        toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getHudname() + ":" + set.getValBoolean()); 
		      if (set.isCombo())
		        toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getHudname() + ":" + set.getValString()); 
		      if (set.isSlider())
		        toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getHudname() + ":" + set.getValDouble());
		      if (set.isHueSlider())
			        toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getHudname() + ":" + set.getValDouble()); 
		      if (set.isSaturationSlider())
			        toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getHudname() + ":" + set.getValDouble()); 
		      if (set.isBrightSlider())
			        toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getHudname() + ":" + set.getValDouble()); 
		    } 
		    try {
		      PrintWriter pw = new PrintWriter(this.saveFile);
		      for (String str : toSave)
		        pw.println(str); 
		      pw.close();
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    } 
		  }


	public void load() {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (String s : lines) {
			String[] args = s.split(":");
			if (s.toLowerCase().startsWith("mod:")) {
				Module m = Artemis.instance.moduleManager.getModuleByName(args[1]);
				if (m != null) {
					m.setToggled(Boolean.parseBoolean(args[2]));
					m.setKey(Integer.parseInt(args[3]));
				}
			} else if (s.toLowerCase().startsWith("set:")) {
				Module m = Artemis.instance.moduleManager.getModuleByName(args[2]);
				if (m != null) {
					Setting set = Artemis.instance.settingsManager.getSettingByName(args[1]);
					if (set != null) {
						if (set.isCheck()) {
							set.setValBoolean(Boolean.parseBoolean(args[3]));
						}
						if (set.isCombo()) {
							set.setValString(args[3]);
						}
						if (set.isSlider()) {
							set.setValDouble(Double.parseDouble(args[3]));
						}
						if (set.isHueSlider()) {
							set.setValDouble(Double.parseDouble(args[3]));
						}
						if (set.isSaturationSlider()) {
							set.setValDouble(Double.parseDouble(args[3]));
						}
						if (set.isBrightSlider()) {
							set.setValDouble(Double.parseDouble(args[3]));
						}
					}
				}
			}
		}
	}
	
}