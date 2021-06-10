package com.imooc.mgallery.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
/**
 * Data Source，operation on XML file
 */
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.imooc.mgallery.entity.Painting;
import com.sun.corba.se.spi.ior.Writeable;


public class XmlDataSource {
    //static静态关键字保证数据全局唯一
    private static List data = new ArrayList();
    private static String dataFile;
    static {
        // painting.xml xml file
        // c:\new style\painting.xml
        // c:\new%20style\painting.xml
        dataFile = XmlDataSource.class.getResource("/painting.xml").getPath();
        reload();


    }

    private static void reload() {
        URLDecoder decoder = new URLDecoder();
        try {
            decoder.decode(dataFile,"UTF-8");
            System.out.println(dataFile);
            //Use Dom4j to manipulate XML
            SAXReader reader = new SAXReader();
            //1. Get Document object
            Document document = reader.read(dataFile);
            //2.Use Xpath to get XML file nodes
             List<Node> nodes = document.selectNodes("/root/painting");
            data.clear();
            for(Node node : nodes) {
                Element element = (Element)node;
                String id = element.attributeValue("id");
                String pname = element.elementText("pname");
                Painting painting = new Painting();
                painting.setId(Integer.parseInt(id));
                painting.setPname(pname);
                painting.setCategory(Integer.parseInt(element.elementText("category")));
                painting.setPrice(Integer.parseInt(element.elementText("price")));
                painting.setPreview(element.elementText("preview"));
                painting.setDescription(element.elementText("description"));
                data.add(painting);
//				System.out.println(id+":" + pname);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *  Get all Paintings
     * @return Painting List
     */
    public static List<Painting> getRawData(){
        return data;
    }
    /**
     * Add new painting
     * @param painting Painting object
     */
    public static void append(Painting painting) {
        //1.Read XML file,get Document object
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document = reader.read(dataFile);
            //2.Create new painting node
            Element root = document.getRootElement();//<root>
            Element p = root.addElement("painting");
            //3.Create painting node's child nodes
            p.addAttribute("id", String.valueOf(data.size()+1));
            p.addElement("pname").setText(painting.getPname());
            p.addElement("category").setText(painting.getCategory().toString());
            p.addElement("price").setText(painting.getPrice().toString());
            p.addElement("preview").setText(painting.getPreview());
            p.addElement("description").setText(painting.getDescription());
            //4. Write to XML file,finish append
            writer=new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
            document.write(writer);
//			System.out.println(dataFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
            reload();//内存与文件数据一致
        }

    }

    /**
     * Update corresponding id's XML painting node
     * @param painting 要更新的油画数据
     * @throws IOException
     */
    public static void update(Painting painting) {
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document=reader.read(dataFile);
            // /root/painting[@id=x]
            List<Node> nodes = document.selectNodes("/root/painting[@id=" + painting.getId() + "]");
            if(nodes.size() == 0) {
                throw new RuntimeException("id=" + painting.getId() + "painting doesn't exist");
            }
            Element p = (Element)nodes.get(0);
            p.selectSingleNode("pname").setText(painting.getPname());
            p.selectSingleNode("category").setText(painting.getCategory().toString());
            p.selectSingleNode("price").setText(painting.getPrice().toString());
            p.selectSingleNode("preview").setText(painting.getPreview());
            p.selectSingleNode("description").setText(painting.getDescription());
            writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
            document.write(writer);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            reload();
        }

    }

    /**
     * Delete painting based on id
     * @param id painting id
     * @throws IOException
     */

    public static void delete(Integer id) {
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document = reader.read(dataFile);
            List<Node> nodes = document.selectNodes("/root/painting[@id=" + id + "]");
            if(nodes.size() == 0) {
                throw new RuntimeException("id=" + id + "painting doesn't exist");
            }
            Element p = (Element)nodes.get(0);
            p.getParent().remove(p);
            writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
            document.write(writer);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(writer!=null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            reload();
        }
    }

    public static void main(String[] args) {
//		new XmlDataSource();
//		List<Painting> ps = XmlDataSource.getRawData();
//		System.out.println(ps);
        Painting p = new Painting();
        p.setPname("Testing");
        p.setCategory(1);
        p.setPrice(4000);
        p.setPreview("/upload/10.jpg");
        p.setDescription("Test description");
        XmlDataSource.append(p);
    }
}
