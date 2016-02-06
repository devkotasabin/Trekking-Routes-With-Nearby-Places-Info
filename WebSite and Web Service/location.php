<?php include("includes/header.php") ?>
<?php include("includes/functions.php")?>
<?php
  $id =$_GET['id'];
  $route_id =$_GET['route_id'];
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

echo "<ul class=\"sitebar-nav\">";
			$location_set = confirm_query("SELECT * FROM location WHERE route_id =$route_id ");
		   while ($location = mysql_fetch_array($location_set))
		   {
		     echo "<li><a href=\"location.php?id=".$location['id']."&"."route_id=".$location['route_id']."\">".$location["name"]."</a></li>";
		 }
		echo "</ul>";	
	?>
</div> <!-- End of sitebar-->
<div id="main-content"> 
<?php
$location_set = confirm_query("SELECT * FROM location WHERE id=$id");
$location = mysql_fetch_array($location_set);
	echo "<h2>".$location['name']."</h2>";	
		echo "<p><b>Description: </b>".$location['description']."</p>";
echo "<table>";
echo "<tr>"."<td>Altitude: "."</td>"."<td>".$location['altitude']."</td>"."</tr>";
echo "<tr>"."<td>Rating: "."</td>"."<td>".$location['rating']."</td>"."</tr>";
echo "</table>";
?>
	</div> <!-- End of main content -->
	<p class="clear-fix">&nbsp;</p>
	</div> <!-- End of content-->
<?php include("includes/footer.php")?>