# SoftwareTesting
We modified the java code in several ways. Initially we wanted to make drop down lists with valid entries for room,
instructor, day, time. The complexities of getting drop down lists to work as parts of a JTable turned out to be too 
complex for the timeframe. It also would have necessitated another button to add valid entries to these lists. 
Instead, we made it so it would simply warn people if they entered a unique entry. This was based either on preset
values for day, or a list of all existing instructors and rooms extracted from the data. This seemed more appropriate
for flexibility and in the warning we tell the user they can simply save to add the new instructor or room to the list
of valid entries. This adds an extra safeguard against human error without getting in the way too much or 
inconveniencing a user who may need to make unique modifications to the database.  This code was added to GraphicalUserInterface
under the comment //EDIT START

ScheduleData was changed in two ways, first some functions were added to help get the list of valid instructor and course names.
Second logiv was added to the detect time conflict portion of the code in order to detect any sort of overlap. This is more accurate than
just checking if begin times match. This could be a beginning or end times orrcuring inside the timeframe of another class or both beginning 
and end being out side or inside another class. This turned out to be harder than anticipated due to the way time values were formatted. 
I'm fairly sure the fringe cases on the modular rollover (12:00 - 1:00) will not be detected.
This code was added to ScheduleData under the comment //EDIT START

We also didn't have time to get into making Junit tests
