import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
   public static void main(String[] args) {
      JUnitCore jUnitCore = new JUnitCore();
      Result result = jUnitCore.run(BlackjackTests.class);
		
      for (Failure failure : result.getFailures()) {
         System.out.println("Number of failed tests: "+failure.toString());
      }
		
      System.out.println("All tests passed: "+result.wasSuccessful());
   }
}  