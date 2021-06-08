package com.imooc.mgallery.utils;

import com.imooc.mgallery.entity.Painting;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Data source object, convert XML file to Java Object
 */
public class XmlDataSource {
    //use static to make sure there is only one copy
    private static List<Painting> data = new ArrayList();
    private static String dataFile;
    static {
        //get painting.xml full path
        dataFile = XmlDataSource.class.getResource("/painting.xml").getPath();
        reload();
    }

    private static void reload(){
        URLDecoder decoder = new URLDecoder();
        try {
            dataFile = decoder.decode(dataFile, "UTF-8");
            //Use DOM4j process xml
            SAXReader reader = new SAXReader();
            //1. Get Document object
            Document document = reader.read(dataFile);
            //2. Use Xpath get XML node
            List<Node> nodes = document.selectNodes("root/painting");
            data.clear();
            for(Node node: nodes){
                Element element = (Element) node;
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
            }

        } catch (UnsupportedEncodingException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public static List<Painting> getRawData(){
        return data;
    }
    public static void append(Painting painting) {
        //1. Read XML file, get Document object
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document = reader.read(dataFile);
            //2. Create painting node
            Element root = document.getRootElement();//<root>
            Element p = root.addElement("painting");
            //3. Create child node for painting node
            p.addAttribute("id", String.valueOf(data.size() + 1));
            p.addElement("pname").setText(painting.getPname());
            p.addElement("category").setText(painting.getCategory().toString());
            p.addElement("price").setText(painting.getPrice().toString());
            p.addElement("preview").setText(painting.getPreview());
            p.addElement("description").setText(painting.getDescription());
            //4. Save to original XML file.
            writer = new OutputStreamWriter(new FileOutputStream(dataFile), "UTF-8");
            document.write(writer);
            System.out.println(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reload(); //make sure data in memory is consistent.
        }
    }
    public static void main(String[] args) {
//        List<Painting> ps = XmlDataSource.getRawData();
        Painting p = new Painting();
        p.setPname("Test Painting");
        p.setCategory(1);
        p.setPrice(4000);
        p.setPreview("upload/10.jpg");
        p.setDescription("hello");
        XmlDataSource.append(p);

    }
}
