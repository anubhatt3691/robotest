package com.testrobo.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.testrobo.demo.model.DemoApplicationModel;


@Service
public class DemoApplicationService{
	

	
	public DemoApplicationModel getOutput(String inputfile) {
		DemoApplicationModel latestDir = new DemoApplicationModel();
		
		File file = new File(inputfile);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		Map<String,Integer> movement = new HashMap<String,Integer>();
		Map<String,Integer> rotation = new HashMap<String,Integer>();
		int x=0,y=0;
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			doc.getDocumentElement().normalize();
			System.out.println(doc.getDocumentElement().getNodeName());
			
			String currdir =null;
			NodeList list = doc.getElementsByTagName("position");
			System.out.println(list);
			for(int i=0;i<list.getLength();i++) {
				Node node = list.item(i);
				System.out.println(node.getChildNodes());
				System.out.println(node.getNodeName());
				Element el = (Element) node;
				System.out.println(el.getAttribute("o"));
				if(el.getAttribute("o").equals("0")) {
					currdir = el.getElementsByTagName("direction").item(0).getTextContent();
					System.out.println(currdir);
					x = Integer.parseInt(el.getElementsByTagName("x").item(0).getTextContent());
					y = Integer.parseInt(el.getElementsByTagName("y").item(0).getTextContent());
					System.out.println("x "+x+" y "+y);
				}else {
					System.out.println(el.getElementsByTagName("L").item(0).getTextContent());
					if(!el.getElementsByTagName("L").item(0).getTextContent().equals("0") && 
							el.getElementsByTagName("R").item(0).getTextContent().equals("0")) {
						rotation.put("L",Integer.parseInt(el.getElementsByTagName("L").item(0).getTextContent()));
					}else if(!el.getElementsByTagName("R").item(0).getTextContent().equals("0") &&
							el.getElementsByTagName("L").item(0).getTextContent().equals("0")) {
						rotation.put("R",Integer.parseInt(el.getElementsByTagName("R").item(0).getTextContent()));
					}
					if(!el.getElementsByTagName("F").item(0).getTextContent().equals("0") && 
							el.getElementsByTagName("B").item(0).getTextContent().equals("0")) {
						
						movement.put("F",Integer.parseInt(el.getElementsByTagName("F").item(0).getTextContent()));
						
					}else if(!el.getElementsByTagName("B").item(0).getTextContent().equals("0") && 
							el.getElementsByTagName("F").item(0).getTextContent().equals("0")) {
						
						movement.put("B",Integer.parseInt(el.getElementsByTagName("B").item(0).getTextContent()));
					}
				}
			}
			
			//calculate theoutput
			latestDir = calculateDir(currdir,rotation,movement,x,y);
			
		}catch(IOException exp) {
			exp.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return latestDir;
	}
	
	public DemoApplicationModel calculateDir(String dir, Map rotation, Map movement,int x,int y) {
		DemoApplicationModel dm = new DemoApplicationModel();
		String finalDir = null;
		int currloc=0;
		if(dir.equals("E")) {
			currloc = 90;
		}else if(dir.equals("W")) {
			currloc = 270;
		}else if(dir.equals("S")) {
			currloc = 180;
		}
		int newdir=currloc; int newx =x; int newy =y;
		List<String> rotateKeys = new ArrayList<String>(rotation.keySet());
		List<String> moveKeys = new ArrayList<String>(movement.keySet());
		
		for(int i=rotateKeys.size()-1;i>=0;i--) {
			int val = (int) rotation.get(rotateKeys.get(i));
			if(rotateKeys.get(i).equals("L")) {
				newdir = (360- val)+newdir;
				if(newdir>360) {
					newdir = newdir -360;
				}
			}else {
				newdir = newdir+ val;
				if(newdir>360) {
					newdir = newdir -360;
				}
			}
			int val1 = (int) movement.get(moveKeys.get(i));
					if(newdir == 270) {
						if(moveKeys.get(i).equals("B")) {
							newx = newx + val1;
						}else {
							newx = newx - val1;
						}
					}else if(newdir == 180) {
						if(moveKeys.get(i).equals("B")) {
							newy = newy + val1;
						}else {
							newy = newy - val1;
						}
					}else if(newdir == 90) {
						if(moveKeys.get(i).equals("B")) {
							newx = newx - val1;
						}else {
							newx = newx + val1;
						}
					}else if (newdir ==0) {
						if(moveKeys.get(i).equals("B")) {
							newy = newy - val1;
						}else {
							newy = newy + val1;
						}
					}
				}
			
			
		
		
		/*	for(int j = moveKeys.size()-1;j>=0;j--) {
			int val = (int) movement.get(moveKeys.get(j));
			if(moveKeys.get(j).equals("B")) {
				newx = newx - val;
			}else {
				newx = newx + val;
			}
		}
		
		Iterator<Map.Entry<String, Integer>> itr = rotation.entrySet().iterator();
		Iterator<Map.Entry<String, Integer>> itr1 = movement.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, Integer> entry = itr.next();
			if(entry.getKey().equals("L")) {
				newdir = (360-entry.getValue())+newdir;
			}else {
				newdir = newdir+entry.getValue();
				if(newdir>360) {
					newdir = newdir -360;
				}
			}
		}*/
		System.out.println("final direction "+newdir);
	/*	while(itr1.hasNext()) {
			Map.Entry<String, Integer> entry= itr1.next();
			if(entry.getKey().equals("B")) {
				newx = newx - entry.getValue();
			}else {
				newx = newx + entry.getValue();
			}
		}*/
		if(newdir==0) {
			dm.setDirection("N");
		}else if(newdir ==90) {
			dm.setDirection("E");
		}else if (newdir ==180) {
			dm.setDirection("S");
		}else if(newdir == 270) {
			dm.setDirection("W");
		}
		dm.setX(newx);
		dm.setY(y);
		System.out.println("final coordinates " + newx +","+y);
		return dm;
	}
	
	public void generateOutputXmlFile(DemoApplicationModel dm) {
		DocumentBuilderFactory dbFactory =
		         DocumentBuilderFactory.newInstance();
		File file = new File("output.xml");
        FileOutputStream out; 
        StreamResult result = null;
        byte[] contentInBytes = null;
		try {
			out = new FileOutputStream(file);
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			//position
			Element pos= doc.createElement("position");
			doc.appendChild(pos);
			//direction
			Element dir = doc.createElement("direction");
			dir.appendChild(doc.createTextNode(dm.getDirection()));
			pos.appendChild(dir);
			//x
			Element x = doc.createElement("x");
			x.appendChild(doc.createTextNode(String.valueOf(dm.getX())));
			pos.appendChild(x);
			//y
			Element y = doc.createElement("y");
			y.appendChild(doc.createTextNode(String.valueOf(dm.getY())));
			pos.appendChild(y);
			
			//doc = dBuilder.parse(file);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        result = new StreamResult(new File("output.xml"));
	        transformer.transform(source, result);
	        StreamResult consoleResult = new StreamResult(System.out);
	        transformer.transform(source, consoleResult);
	        result.setOutputStream(out);
	        
		} catch (ParserConfigurationException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
