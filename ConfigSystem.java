package com.artemis.ui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.artemis.Artemis;
import com.artemis.altmanager.Alt;
import com.artemis.file.Configer;
import com.artemis.fontrenderer.UnicodeFontRenderer;
import com.artemis.logger.Logger;
import com.artemis.logger.Logger.LogType;
import com.artemis.module.Module;
import com.artemis.settings.Setting;
import com.artemis.utils.RenderUtil1;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class ConfigSystem extends GuiScreen{
    
     public int offset;
     public static Scanner scanner;
     UnicodeFontRenderer ufr;
     CleanTextField textField;
     CleanTextField renameField;
     CleanTextField urlField;
     public File dir;
     public File duplicateFile;
     public File rename;
     public boolean save =  false;
     public boolean settings =  false;
     public boolean renameConfig =  false;
     public String selectedConfig = "";
     public File selectedConfig2 = null;
     
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		 if (this.ufr == null)
		        this.ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0, 2.0F, 1.0F); 
		 
		 if (Mouse.hasWheel()) {
	            int wheel = Mouse.getDWheel();
	            if (wheel < 0) {
	                this.offset += 6;
	                if (this.offset < 0) {
	                    this.offset = 0;
	                }
	                if (this.offset > 70) {
	                    this.offset = 70;
	                }
	            } else if (wheel > 0) {
	                this.offset -= 6;
	                if (this.offset < 0) {
	                    this.offset = 0;
	                }
	                if (this.offset > 70) {
	                    this.offset = 70;
	                }
	            }
	        }
		 
		  
		 
		        float x2 = 260;
                        float y2 = 32;
                        boolean hovered = mouseX >= x2 && mouseY >= y2 && mouseX < x2 + mc.fontRendererObj.getStringWidth("New") && mouseY < y2 + mc.fontRendererObj.FONT_HEIGHT;
		
			
	                this.drawRect(240, 20, 560, 300, 0xFF222222);
			this.drawRect(240, 20, 560, 55, 0xFF111111);
			this.drawRect(450, 20, 560, 300, 0xFF111111);
			RenderUtil1.drawRoundedRect(438, 58+   offset * 3, 440, 85+ offset * 3, 1, Color.GRAY);
			if(!this.save) {
			   ufr.drawString("New", 260, 32, hovered ? 0x494949 : -1);
			}else if(this.save) {
		           ufr.drawString("Back", 260, 32, hovered ? 0x494949 : -1);
			} 
			int count = 0;
			if (this.settings) {				
        		if(this.isMouseOnConfig(mouseX, 460, mouseY, 50, "Delete")) {
          		 ufr.drawString("Delete", 460, 50, 0x494949);	
          		}else {
          	           ufr.drawString("Delete", 460, 50, -1);	
          		}
        		if(this.isMouseOnConfig(mouseX, 500, mouseY, 50, "Duplicate")) {
       		           ufr.drawString("Duplicate", 500, 50, 0x494949);	
       		        }else {
       			   ufr.drawString("Duplicate", 500, 50, -1);	
       		        } 
        		if(this.isMouseOnConfig(mouseX, 500, mouseY, 65, "Back")) {
        		    ufr.drawString("Back", 500, 65, 0x494949);	
        		}else {
        	            ufr.drawString("Back", 500, 65, -1);	
        		}
        		if(this.isMouseOnConfig(mouseX, 460, mouseY, 65, "Save")) {
       		            ufr.drawString("Save", 460, 65, 0x494949);	
       		        }else {
       			    ufr.drawString("Save", 460, 65, -1);	
       		        }
        		if(this.isMouseOnConfig(mouseX, 460, mouseY, 80, "Rename")) {
          		     ufr.drawString("Rename", 460, 80, 0x494949);	
          		}else {
          	             ufr.drawString("Rename", 460, 80, -1);	
          		}
                   }
		
		    GL11.glPushMatrix();
		    this.prepareScissorBox(0.0f, 33.0f, width, height - 3);
		    GL11.glEnable(3089);
			for(File file : dir.listFiles()) {
		        float x = 260;
			float y = 60 + count * 17- this.offset;
		        String filename = file.getName().replace(".txt", "");
			boolean hoverd = mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(filename) && mouseY < y + mc.fontRendererObj.FONT_HEIGHT;
			RenderUtil1.drawRoundedRect(250, 58+ (count * 17) - offset, 430, 72+ (count * 17)- offset, 2, Color.DARK_GRAY.darker());  	
			ufr.drawString(file.getName().replace(".txt", ""), 260, 60 + count * 17- this.offset, hoverd ? 0x494949 : -1);		  	
				count++;
		    }
			GL11.glDisable(3089);
			GL11.glPopMatrix();
			if(this.renameConfig) {
				 this.renameField.draw(ufr);
				 float x = 460;
		                 float y = 120;
				 boolean hovered2 = mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth("Save") && mouseY < y + mc.fontRendererObj.FONT_HEIGHT;
				 ufr.drawString("Save", 460, 120, hovered2 ? 0x494949 : -1);
			}
			if(this.save) {
				 float x = 300;
		                 float y = 32;
				 boolean hovered2 = mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth("Save") && mouseY < y + mc.fontRendererObj.FONT_HEIGHT;
			         textField.draw(ufr);
			         urlField.draw(ufr);
				 ufr.drawString("Save", 300, 32, hovered2 ? 0x494949 : -1);
				
				if(this.isMouseOnConfig(mouseX, 414, mouseY, 32, "Load")) {
         		             ufr.drawString("Load", 414, 32, 0x494949);	
         		        }else {
         			     ufr.drawString("Load", 414, 32, -1);	
         		        }	
			     }
	                  }
	 public void prepareScissorBox(float x2, float y2, float x22, float y22) {
	        ScaledResolution scale = new ScaledResolution(this.mc);
	        int factor = scale.getScaleFactor();

	        GL11.glScissor((int)(x2 * (float)factor), (int)(((float)scale.getScaledHeight() - y22) * (float)factor) + 190, (int)((x22 - x2) * (float)factor), (int)((y22 - y2) * (float)factor) - 235);

	 }
	
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        textField.keyTyped(typedChar, keyCode);      
        urlField.keyTyped(typedChar, keyCode);      
        renameField.keyTyped(typedChar, keyCode);
        
        super.keyTyped(typedChar, keyCode);
    }
	
	 public void switchSave() {
	     save  = !save;
	 }
	 
	 public void switchSettings() {
	     settings  = !settings;
	 }
	 
	 public void switchRename() {
             renameConfig  = !renameConfig;
	 }
	
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
	   if(textField != null) {
               textField.mouseClicked(mouseX, mouseY, ufr);
           }
	   if(urlField != null) {
	       urlField.mouseClicked(mouseX, mouseY, ufr);
           }
	   if(this.renameField != null) {
	       renameField.mouseClicked(mouseX, mouseY, ufr);
           }
	    
	if(button == 0) {
	      if(this.settings) {
	            if(this.isMouseOnConfig(mouseX, 500, mouseY, 65, "Back")) {
   		    this.settings = false;
   		    this.renameConfig = false;
   		   }
			if(this.isMouseOnConfig(mouseX, 460, mouseY, 50, "Delete")) {
	   		      Configer.instance.configRemove(this.selectedConfig.replace(".txt", ""));
	   		   }
			if(this.isMouseOnConfig(mouseX, 500, mouseY, 50, "Duplicate")) {
	   		      this.duplicate(this.selectedConfig.replace(".txt", "") + " - Duplicate " + " " + RandomUtils.nextLong(4444L, 100000000L), this.selectedConfig2);
	   		   }
			if(this.isMouseOnConfig(mouseX, 460, mouseY, 65, "Save")) {
      		        this.save(selectedConfig2);
      		           }
			if(this.isMouseOnConfig(mouseX, 460, mouseY, 80, "Rename")) {
				this.renameConfig = true;
     		}
			if(this.isMouseOnConfig(mouseX, 460, mouseY, 120, "Save") && this.renameConfig) {
				//this.renameConfig = true;
     		   this.rename(this.renameField.getText(), this.selectedConfig2);
     		}
	    }
			if(this.save) {
				if(this.isMouseOnConfig(mouseX, 414, mouseY, 32, "Load")) {
				  this.onlineConfig(this.urlField.getText());
	     		}
			}
			int count = 0;
			for(File file : dir.listFiles()) {
			
		    float x = 260;
	         float y = 60 + count * 17- this.offset;
             String filename = file.getName().replace(".txt", "");
	         if(mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(filename) && mouseY < y + mc.fontRendererObj.FONT_HEIGHT) {
			    	  Configer.instance.Config(filename);
	                  Artemis.instance.config = filename;
	         }
	         count++;
			}
			 float x = 260;
	         float y = 32;
	            
		         if(mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth("New") && mouseY < y + mc.fontRendererObj.FONT_HEIGHT) {
		          // Configer.instance.configSave(this.textField.getText());
		              this.switchSave();
		              this.textField.setText("");
		              this.renameField.setText("");
		              this.urlField.setText("");
		         }
		         float x2 = 300;
		         float y2 = 32;
		         if(this.save) {
			     if(mouseX >= x2 && mouseY >= y2 && mouseX < x2 + mc.fontRendererObj.getStringWidth("Save") && mouseY < y2 + mc.fontRendererObj.FONT_HEIGHT) {
			    //     System.out.println(textField.getText());
			    	 Configer.instance.configSave(this.textField.getText());
			             // this.switchSave();
			     }
		     }
		}else if(button == 1) {
			int count = 0;
			for(File file : dir.listFiles()) {
				
			    float x = 260;
		         float y = 60 + count * 17;
	             String filename = file.getName().replace(".txt", "");
		         if(mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(filename) && mouseY < y + mc.fontRendererObj.FONT_HEIGHT) {
				  this.renameConfig = false;
		        	 this.selectedConfig2 = file;
		        	 this.selectedConfig = file.getName();	 
                 this.settings = true;
		         }
		         count++;
				}
		}
	}
	public boolean isMouseOnConfig(int mouseX, int x, int mouseY, int y, String text) {
		return mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(text) && mouseY < y + mc.fontRendererObj.FONT_HEIGHT;
	}
	@Override
	public void initGui() {
	dir = new File(Minecraft.getMinecraft().mcDataDir, "Artemis Configs");
    textField = new CleanTextField(350, 32, "", "Set Name");
    urlField = new CleanTextField(450, 32, "", "URL");
    renameField = new CleanTextField(460, 100, "", "Rename");
	super.initGui();
	}
	public void duplicate(String filename, File original) {
		duplicateFile = new File(dir, filename + ".txt");
		if (!duplicateFile.exists()) {
			try {
				duplicateFile.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}else {
			Artemis.addChatMessage("File already exists!");
			Logger.instance.log("File already exists!", LogType.ERROR);
			return;
		}
		   ArrayList<String> toSave = new ArrayList<>();
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(original));
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
			  toSave.add(s);
		}
		
	
		    try {
		      PrintWriter pw = new PrintWriter(this.duplicateFile);
		      for (String str : toSave)
		        pw.println(str); 
		      pw.close();
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    } 
	}
	public void rename(String filename, File original) {
		rename = new File(dir, filename + ".txt");
		if (!rename.exists()) {
			try {
				rename.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}else {
			Artemis.addChatMessage("File already exists!");
			Logger.instance.log("File already exists!", LogType.ERROR);
			return;
		}
		   ArrayList<String> toSave = new ArrayList<>();
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(original));
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
			  toSave.add(s);
		}
		
	
		    try {
		      PrintWriter pw = new PrintWriter(this.rename);
		      for (String str : toSave)
		        pw.println(str); 
		      pw.close();
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    } 
		    original.delete();
	}
	 public void save(File saveTo) {
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
		      PrintWriter pw = new PrintWriter(saveTo);
		      for (String str : toSave)
		        pw.println(str); 
		      pw.close();
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    } 
		  }
	 
	 public void onlineConfig(String URL) {
		    try {
	            final URL url = new URL(URL);
	            try {
	                this.scanner = new Scanner(url.openStream());
	                while (this.scanner.hasNextLine()) {
	                    

	                    final String creds = this.scanner.nextLine();

	                    if (creds.contains(":"))
	                    {
	                        String[] args = creds.split(":");

	                      
	                			if (creds.toLowerCase().startsWith("mod:")) {
	                				Module m = Artemis.instance.moduleManager.getModuleByName(args[1]);
	                				if (m != null) {
	                					m.setToggled(Boolean.parseBoolean(args[2]));
	                					m.setKey(Integer.parseInt(args[3]));
	                				}
	                			} else if (creds.toLowerCase().startsWith("set:")) {
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
	                

	            } catch (IOException e) {
	            	e.printStackTrace();
	            	Artemis.addChatMessage("Scanning URL Error!");
					Logger.instance.log("Scanning URL Error!", LogType.ERROR);
	            	}
	        } catch (MalformedURLException e) {
	        	e.printStackTrace();
	        	Artemis.addChatMessage("URL invalid!");
				Logger.instance.log("URL invalid!", LogType.ERROR);
	        }
	 }
}
