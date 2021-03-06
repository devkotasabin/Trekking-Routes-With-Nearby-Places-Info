<?php include("includes/header.php") ?>
<?php include("includes/functions.php")?>
<?php
  $id =$_GET['id'];
?>
	<title>Trek Route Project</title>
<link rel="stylesheet" type="text/css" href="stylesheets/style.css">
</head>
<body>
<div id="wrap">
<div class="hearder-nav">
	<div class="logo">
		TrekRoute Logo
		</div>
<div class="search">
	<form  action="process.php">
    <input class="text" size="35" type="text" name="search" >&nbsp;&nbsp;
  	<input class="submit" type="submit" value="Search">
</div>
<p class="clear-fix">&nbsp;</p>
</div> <!-- End of header-nav -->

<div id="menu">
<ul>
		<li><a href="index.php">Home</a></li>
	<?php
	$route_set = confirm_query("SELECT * FROM route LIMIT 5");
	while($route = mysql_fetch_array($route_set)){
		if ($temp != $route["type"])
		{
			$url = "route-type.php?";
			$temp = urlencode($route["type"]);
			$url .= "type=".$temp;
	 		echo "<li><a href=\"$url\">".ucfirst($route["type"])."</a></li>";
	    } 
		$temp = $route["type"];
		} 
	?>
	</ul>
</div> <!-- End of menu -->

<div id="content">
<div id="sidebar"><p>	
<?php
$location_set = confirm_query("SELECT * FROM location WHERE route_id=$id");
	echo "<ul class=\"sitebar-nav\">";
while($location = mysql_fetch_array($location_set))
{
	echo "<li><a href=\"location.php?id={$location['id']}&route_id=$id\">".$location["name"]."</a></li>";
}
echo "</ul>";
?>
</div> <!-- End of sitebar-->
<div id="main-content"> 
<?php
$route_set = confirm_query("SELECT * FROM route WHERE id=$id");
$route = mysql_fetch_array($route_set);
echo "	<div class=\"route-info\">";
echo "<h2>".ucfirst($route['name'])."</h2>";
echo $route['description'];
echo "<p style=\"font-size: 18px;\"><b>More Information About This Route</b></p>";
echo "<table >";
echo "<tr>";
echo "<td>Estimated Cost: </td>"."<td>$".$route['cost']."</td>";
echo "</tr>";
if ($route['difficulty']!=0)
{ 
	echo "<tr>";
	echo "<td>Difficulty: </td><td>{$route['difficulty']}</td>";
	echo "</tr>";
}
echo "<tr>";
echo"<td>Days: </td><td>{$route['days']}</td>";
echo "</tr>";
echo "</table>";
echo "</div>";

$location_set = confirm_query("SELECT * FROM location WHERE route_id = $id");
$count = 1;
while ($location = mysql_fetch_array($location_set))
{
	echo "Day ".$count++.": To ".$location['name']."<br/>" ;
}
?>	
</p>
	</div> <!-- End of main content -->
	<p class="clear-fix">&nbsp;</p>
	</div> <!-- End of content-->
<?php include("includes/footer.php")?>