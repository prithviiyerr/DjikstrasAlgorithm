#! /usr/bin/bash
#
#  Invocation:  gradeJ5.sh <student zip or tar file>
#               gradeJ5.sh -clean
#
#  Prerequisites:  the Oracle JDK must be installed, and its bin directory
#                  must be in your path
#
#  To use this script (on Linux):
#    - create a testing directory
#    - copy the zip or tar file containing your solution to the test directory
#    - copy the posted grading jars (SSADGen.jar and LogComparator.jar) to the test directory
#    - put this script in that directory
#    - if necessary, run chmod a+x to make the script executable
#    - execute the command given above
#
#  This script will run your solution on a sequence of five random graphs, and generate
#  a score for each, producing a final report file named PID.txt (assuming you've named
#  your zip/tar file using your PID as the first part of the name (as the Curator would
#  name your submission).
#
#  If you get an error message about the build failing, then your zip file is defective.
#  That is one reason you should, at least, use this script to test your submission.
#
########################################################################
# Configuration variables:
javaMain="SSAD.java"
classMain="SSAD.class"
logFile="report.txt"
GENtool="SSADGen.jar"
COMPtool="LogComparator.jar"
STUfile="results.txt"
buildLog="buildLog.txt"
testLog="testLog.txt"
separator="##############################################################"

##################################### fn to extract PID from file name
#                 param1: (possibly fully-qualified) name of file
getPID() { 

   fname=$1
   # strip off any leading path info
   fname=${fname##*/}
   # extract first token of file name
   sPID=${fname%%.*}
}

############################################# fns to check for zip/tar file
#                 param1:  name of file to be checked
isZip() {

   mimeType=`file -b --mime-type $1`
   [[ $mimeType == "application/zip" ]]
}

isTar() {

   mimeType=`file -b --mime-type $1`
   if [[ $mimeType == "application/x-tar" ]]; then
     return 0
   fi
   if [[ $mimeType == "application/x-gzip" ]]; then
     return 0
   fi
   return 1
}

########################################################## clean()
clean() {
   
   rm -Rf *.java *.class *.txt
   for tfile in ./*
   do
      if [[ -d $tfile ]]; then
         rm -Rf $tfile
      fi
   done
}

########################################################################
#
#  Check for valid command-line parameters to the script:
   if [[ $# -ne 1 ]]; then
      
      echo "Error:  incorrect command-line parameters."
      echo "   gradeJ5.sh <your zip or file>"
      echo "   gradeJ5.sh -clean"
      echo "Read the header comment for more information."
      exit -1
   fi
   
   if [[ $1 == "-clean" ]]; then
      clean
      exit 0
   fi
   
# Must be grading... initialize log and check for student zip file
   studentSubmission=$1
   getPID $studentSubmission
   logFile="$sPID.txt"
   echo "Grading:  $1" > $logFile
   echo -n "Time:     " >> $logFile
   echo `date` >> $logFile
   echo $separator >> $logFile
   echo >> $logFile
   
########################################################## prepare for build   

#  Check existence of comparison tool:
   toolsOK="yes"
   if [[ ! -e $COMPtool ]]; then
      echo "Error:  $COMPtool is not present..." >> $logFile
      toolsOK="no"
   fi

#  Check existence of generator tool:
   if [[ ! -e $GENtool ]]; then
      echo "Error:  $GENtool is not present..." >> $logFile
      toolsOK="no"
   fi
   
   if [[ tooksOK == "no" ]]; then
      exit -6
   fi
   
   echo "Beginning build process..." > $buildLog
   echo >> $buildLog
   if [[ ! -e $studentSubmission ]]; then
      echo "   Error:  $studentSubmission is missing." >> $buildLog
      cat $buildLog >> $logFile
      exit -2
   else
      isZip $studentSubmission
      if [[ $? -eq 0 ]]; then
         echo "   Unzipping student submission: $studentSubmission" >> $buildLog
         unzip -o $studentSubmission >> $buildLog
         if [[ $? -ne 0 ]]; then
            echo "   Error unzipping the file... giving up." >> $buildLog
            cat $buildLog >> $buildLog
            exit -2
         fi
      else
         isTar $studentSubmission
         if [[ $? -eq 0 ]]; then
            echo "   Untarring student submission: $studentSubmission" >> $buildLog
            tar xf $studentSubmission >> $buildLog
            if [[ $? -ne 0 ]]; then
               echo "   Error unzipping the file... giving up." >> $buildLog
               cat $buildLog >> $logFile
               exit -2
            fi
         else
            echo "   Error:  $studentSubmission is neither a zip file nor a tar file." >> $logFile
            echo "   Giving up..." >> $buildLog
            cat $buildLog >> $logFile
            exit -2
         fi
      fi
   fi

################################################################## build

#  List directory contents before beginning grading:
   echo "Contents of student submission..." >> $buildLog
   if [[ -e $javaMain ]]; then
      ls -l *.java >> $buildLog
      echo >> $buildLog
   else
      echo "Panic!!  $javaMain is not in correct directory..." >> $buildLog
      echo "         Your zip or tar file is malformed." >> $buildLog
      echo >> $buildLog
      echo "The test directory looks like this:" >> $buildLog
      ls -l >> $buildLog
      cat $buildLog >> $logFile
      exit -5
   fi
   if [[ -e *.class ]]; then
      ls -l *.class >> $buildLog
   fi
   
#  Remove any Java .class files
   rm -Rf *.class
      
#  Attempt a build; this should compile everything:
   echo -n "Compiling student submission:  " >> $buildLog
   echo "javac $javaMain" >> $buildLog
   javac $javaMain >> $buildLog 2>&1

#  See if the main class file was created:
   if [[ ! -e $classMain ]]; then
      echo "   Error:  $classMain not found; the build failed." >> $buildLog
      cat $buildLog >> $logFile
      exit -6
   fi

################################################################## perform testing

   nCases=5
   inputFiles=("Graph01.txt" "Graph02.txt" "Graph03.txt" "Graph04.txt" "Graph05.txt")
   outputFiles=("SSAD01.txt" "SSAD02.txt" "SSAD03.txt" "SSAD04.txt" "SSAD05.txt")
   refFiles=("refSoln01.txt" "refSoln02.txt" "refSoln03.txt" "refSoln04.txt" "refSoln05.txt")
   compFiles=("compare01.txt" "compare02.txt" "compare03.txt" "compare04.txt" "compare05.txt")
   
   echo "Testing details follow..." > $testLog
   echo >> $testLog
   
   index=0
   while [[ "$index" -lt "$nCases" ]]
   do
   
      # set names for graph file, reference log file, student log file, and comparison log file:
      graphFile=${inputFiles[$index]}
      refLogFile=${refFiles[$index]}
      stuLogFile=${outputFiles[$index]}
      compLogFile=${compFiles[$index]}
      
      # generate a test graph and reference results
      echo "Generating graph file and reference log file: $graphFile and $refLogfile" >> $testLog
      java -jar $GENtool $graphFile $refLogFile
     
      killed="no"
      echo "Running student solution on $graphFile..." >> $testLog
      timeout -s SIGKILL 60 java SSAD $graphFile $stuLogFile >> $testLog 2>&1
		timeoutStatus="$?"
		# echo "timeout said: $timeoutStatus"
		if [[ $timeoutStatus -eq 124 || $timeoutStatus -eq 137 ]]; then
			echo "The test of your solution timed out after 60 seconds." >> $testLog
			echo "Valgrind testing will NOT be done." >> $testLog
			killed="yes"
		elif [[ $timeoutStatus -eq 134 ]]; then
			echo "The test of your solution was killed by a SIGABRT signal." >> $testLog
			echo "Possible reasons include:" >> $testLog
			echo "    - a segfault error" >> $testLog
			echo "    - a serious memory access error" >> $testLog
			echo "Valgrind testing will NOT be done." >> $testLog
			killed="yes"
      elif [[ killed == "yes" ]]; then
         echo "The test of your solution was killed..." >> $testLog
		fi
      ((index +=1))

      #  Check existence of student results file:
      if [[ ! -e $stuLogFile ]]; then
         echo "Error:  $stuLogFile was not created." >> $testLog
         echo >> $testLog
      else
         #  Compare output:
         echo "Comparing reference and student output files..." >> $testLog
         java -jar LogComparator.jar $index $refLogFile $stuLogFile > $compLogFile
         if [[ ! -e $compLogFile ]]; then
            echo "   Error: $compLogFile was not created!" >> $testLog
         else
            echo "Results from comparison:" >> $testLog
            echo >> $testLog
            cat $compLogFile >> $testLog
         fi
         echo >> $testLog
      fi
      echo $separator >> $testLog

   done
   
################################################################## prepare final log file

   #  Gather the scores 
   echo ">>Scores from testing<<" >> $logFile
   grep "Score:" $testLog >> $logFile
   echo >> $logFile
   echo $separator >> $logFile

   # append test details to log file   
   cat $testLog >> $logFile

   # append build details to log file
   cat $buildLog >> $logFile

   exit 0
