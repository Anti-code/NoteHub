/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.File;
import java.io.Serializable;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author stupid
 */
@ManagedBean
@SessionScoped
public class SystemBean implements Serializable{
  private Double systemload;
  private Float ram_usage;
  private float disk_usage;
  private List<String> todo_list=new ArrayList<>();
  private String todo_item;
  OperatingSystemMXBean os =java.lang.management.ManagementFactory.getOperatingSystemMXBean();
  
  public void addTodo(){
    if(!todo_item.isEmpty())
      todo_list.add(todo_item);
  }
    public Double getSystemload() {
    systemload=os.getSystemLoadAverage()*100;
    return systemload;
  }

  public void setSystemload(Double systemload) {
    this.systemload = systemload;
  }

  public Float getRam_usage() {
    Runtime r=Runtime.getRuntime();
    ram_usage=((float)(r.totalMemory()-r.freeMemory())/r.totalMemory())*100;
    
    return ram_usage;
  }

  public void setRam_usage(Float ram_usage) {
    this.ram_usage = ram_usage;
  }

  public float getDisk_usage() {
    File f=new File("/");
    disk_usage=((float)(f.getTotalSpace()-f.getFreeSpace())/f.getTotalSpace())*100;
    
    return disk_usage;
  }

  public void setDisk_usage(float disk_usage) {
    this.disk_usage = disk_usage;
  }

  public List<String> getTodo_list() {
    
    return todo_list;
  }

  public void setTodo_list(List<String> todo_list) {
    this.todo_list = todo_list;
  }

  public String getTodo_item() {
    return todo_item;
  }

  public void setTodo_item(String todo_item) {
    this.todo_item = todo_item;
  }


  
  
  
}
