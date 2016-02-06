<?php
$connection = mysql_connect('localhost','root','');
if (!$connection)
{
	echo "<h2/>Database connection error</h2>";
}
$db_select = mysql_select_db('trekking',$connection);
if(!$db_select)
{
	echo "Database selection error";
}
?>
<!DOCTYPE html>
<html>
<head>