//
// dump_image.java
//

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class dump_image {
	static final String CRLF = "\r\n";
	static final int MAX_COLUMN = 16;
	static int transparentColor = 0xFFFE;
	
	dump_image(String path, String varName) throws IOException {
		File file = new File(path);
		BufferedImage bi = ImageIO.read(file);
		int width = bi.getWidth();
		int height = bi.getHeight();
		boolean hasAlpha = bi.getType() == BufferedImage.TYPE_4BYTE_ABGR;
		if (varName == null) {
			varName = file.getName();
			varName = varName.substring(0, varName.lastIndexOf('.')).toLowerCase();
			varName = varName.replaceAll("[\\s-]", "_");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("#include <M5Stack.h>" + CRLF);
		sb.append(CRLF);
		sb.append("#define " + varName.toUpperCase() + "_WIDTH " + width + CRLF);
		sb.append("#define " + varName.toUpperCase() + "_HEIGHT " + height + CRLF);
		sb.append(CRLF);
		sb.append("const uint16_t PROGMEM " + varName + "[] = {" + CRLF);
		int col = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (col == 0) {
					sb.append("\t"); // indent
				}
				int rgb = bi.getRGB(x, y);
				int color16;
				if (hasAlpha && ((rgb & 0xFF000000) == 0)) {
					color16 = transparentColor;
				} else {
					color16 = color32To16(rgb);
				}				
				sb.append(String.format("0x%04X,", color16));
				if (++col >= MAX_COLUMN) {
					col = 0;
					sb.append(CRLF);
				}
			}
		}
		if (col != 0) {
			sb.append(CRLF);
		}
		sb.append("};" + CRLF);

		System.out.print(sb.toString());
	}

	int color32To16(int rgb) {
		return color565((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
	}
	
	int color565(int r, int g, int b) {
		return ((r & 0xF8) << 8) | ((g & 0xFC) << 3) | (b >> 3);
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("usage: java -jar dump_image.jar <image file path> [<var name>]");
		} else {
			String path = args[0];
			String varName =  (args.length >= 2) ? args[1] : null;
			new dump_image(path, varName);
		}
	}
}
