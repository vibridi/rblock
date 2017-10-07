package com.vibridi.rblock.helpers;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XMLUtils {

	public static Object applyXPath(Document doc, String request, QName returnType) throws IOException {
		return applyXPath(doc.getDocumentElement(), request, returnType);
	}
	
	public static Object applyXPath(Node node, String request, QName returnType) throws IOException {
		if (request == null)
			return null;

		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(request);
			return expr.evaluate(node, returnType);
			
		} catch(XPathExpressionException e) {
			throw new IOException(e);
		}
	}

}
