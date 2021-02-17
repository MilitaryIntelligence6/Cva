package main

import (
	"fmt"
	"os"
	"os/exec"
	"path"
)

/**
 * 获取shell路径, 用来获得输入源文件路径;
 */
func HandleShellPath() string {
	var currentPath, err = os.Getwd()
	if err != nil {
		panic(err)
	}
	return currentPath
}

/**
 * 获取exe所在路径, 用来获得整个app打包后的路径;
 */
func HandleHomePath() string {
	homePath, err := exec.LookPath(os.Args[0])
	if err != nil {
		panic(err)
	}
	var homeDir = path.Dir(homePath)
	fmt.Printf("homeDir is %s\n", homeDir)
	return homeDir
}
