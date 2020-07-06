package coffee.weneed.founddiamonds.file;

import java.io.Closeable;
import java.io.IOException;
import java.text.MessageFormat;
import coffee.weneed.founddiamonds.FoundDiamonds;

public class FileUtils {

	private FoundDiamonds fd;

	public FileUtils(FoundDiamonds fd) {
		this.fd = fd;
	}

	public void close(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException ex) {
				fd.getLog().warning(MessageFormat.format("Failure to close a file stream, {0} ", ex));
			}
		}
	}
}
