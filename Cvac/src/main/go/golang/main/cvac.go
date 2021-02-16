package main

import (
	// 需要将该 pkg 放入%GOPATH%/src/目录下
	// pkg目录应该也是可以的, 我没有试过;
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
