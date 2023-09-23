import java.util.Properties;
import java.io.InputStream;
import com.boomi.execution.ExecutionUtil;
logger = ExecutionUtil.getBaseLogger();

//clean IR_TALEND_OUTPUT

def IR_TALEND_OUTPUT_PATH = ExecutionUtil.getDynamicProcessProperty("IR_TALEND_OUTPUT")+"\\"+ExecutionUtil.getDynamicProcessProperty("CMMS_ID")+"\\"+ExecutionUtil.getDynamicProcessProperty("INTERFACE_NAME");
logger.info("CLEANING DIRECTORY OF IR_TALEND_OUTPUT_PATH : "+IR_TALEND_OUTPUT_PATH);

// Function to delete a directory and its contents recursively
try {
     def dir = new File(IR_TALEND_OUTPUT_PATH)
    if (dir.exists() && dir.isDirectory()) {
        dir.eachFile { file ->
            try {
                if (file.name == "ARC") {
                    // If the file is a directory named "ARC", only delete its contents
                    if (file.isDirectory()) {
                        deleteDirectoryContents(file)
                    }
                } else {
                    // For other directories and files, perform regular deletion
                    if (file.isFile()) {
                        logger.info("Deleting file: ${file.name}")
                        file.delete()
                        logger.info("Deleted file: ${file.name}")
                    } else if (file.isDirectory()) {
                        logger.info("Deleting directory: ${file.name}")
                        deleteDirectory(file) // Call a function to delete subdirectories
                        logger.info("Deleted directory: ${file.name}")
                    }
                }
            } catch (Exception e) {
                logger.severe("An error occurred while processing: ${file.name}. Error: ${e.message}")
            }
        }
    } else {
        logger.info("Directory not found or is not a directory")
    }
} catch (Exception e) {
    logger.severe("An error occurred while processing the directory. Error: ${e.message}")
}

// Function to delete a directory and its contents recursively
def deleteDirectory(File dir) {
    dir.eachFile { file ->
        try {
            if (file.isFile()) {
                logger.info("Deleting file: ${file.name}")
                file.delete()
                logger.info("Deleted file: ${file.name}")
            } else if (file.isDirectory()) {
                deleteDirectory(file) // Recursively delete subdirectories
            }
        } catch (Exception e) {
            logger.severe("An error occurred while deleting: ${file.name}. Error: ${e.message}")
        }
    }
    dir.delete()
}

// Function to delete only the contents of a directory
def deleteDirectoryContents(File dir) {
    dir.eachFile { file ->
        try {
            if (file.isFile()) {
                logger.info("Deleting file: ${file.name}")
                file.delete()
                logger.info("Deleted file: ${file.name}")
            } else if (file.isDirectory()) {
                deleteDirectoryContents(file) // Recursively delete subdirectories' contents
            }
        } catch (Exception e) {
            logger.severe("An error occurred while deleting: ${file.name}. Error: ${e.message}")
        }
    }
}

