package main

import (
	myPkg "MyPkg/PkgTest/test"
	"fmt"
	"os/exec"
)

func main() {
	//handler.
	var shellDir string = HandleShellPath()
	fmt.Printf("shell path is %s\n\n", shellDir)

	var homeDir string = HandleHomePath()
	fmt.Printf("home path is %s\n\n", homeDir)

	cmd := exec.Command("java -version")
	// 执行命令，返回命令是否执行成功
	var output, err = cmd.Output()
	if err != nil {
		// 命令执行失败
		panic(err)
	}
	fmt.Printf("%s\n", output)
	myPkg.EchoPkg()
}
