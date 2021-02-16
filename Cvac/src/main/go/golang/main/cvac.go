package main

import (
	mypkg "MyPkg/PkgTest/test"
	"fmt"
)

func main() {
	//handler.
	echoPath()
	fmt.Println()
	echoExePath()
	mypkg.EchoPkg()
}
