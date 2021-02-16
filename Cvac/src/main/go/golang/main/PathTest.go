package main

import (
	"fmt"
	"os"
	"os/exec"
)

/**
 * 获取shell路径, 用来获得输入源文件路径;
 */
func echoPath() {
	var currentPath, err = os.Getwd()
	fmt.Printf("current path is %s\n", currentPath)
	if err != nil {
		fmt.Printf("catch error %s", err)
	}
}

/**
 * 获取exe所在路径, 用来获得整个app打包后的路径;
 */
func echoExePath() {
	path, err := exec.LookPath(os.Args[0])
	fmt.Printf("exe path is %s", path)
	if err != nil {
		fmt.Printf("catch error %s", err)
	}
}
