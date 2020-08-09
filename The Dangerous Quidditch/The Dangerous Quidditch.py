# -*- coding: UTF-8 -*-

# sprite : 컴퓨터 그래픽스에서 스프라이트(영어: sprite)는 영상 속에 작은 2차원 비트맵이나 애니메이션을 합성하는 기술이다. 초기에는 2D 비디오 게임에서 비디오 영상과 분리된 별도의 그래픽스 객체를 의미했지만, 오늘날에는 다양한 층의 이미지, 텍스트 등을 각각의 우선순위를 두고 합성하는 기술을 통틀어 일컫는다.
# pip install pygame

import game1
import game2

from pygame_functions import * # pygame_functions 튜토리얼 (https://www.youtube.com/watch?v=gmBW_AVDsNY&feature=youtu.be&list=PLeOSHd3t9lzKr4O3A3Q7OZyf8QwyCALyn 참조)
                               # pygame_functions git hub (https://github.com/StevePaget/Pygame_Functions 참조)
                               # pygame을 쉽게 사용 가능.

# 상수 선언
LOSE_CODE = 0

def __main__() :
    ##### 게임 초기 설정 #####
    screenSize(700, 700)
    pygame.display.set_caption("The Dangerous Quidditch")  # 게임창의 title
    #########################

    # 게임 1 스토리 진행
    storyImageList = []
    for storyNum in range(1, 12):
        storyImageList.append("./images/story/story1/story" + str(storyNum) + ".png")
    story(storyImageList)

    while True :
        gameResult = game1.game()   # 게임 1을 호출한 뒤 게임 결과를 받음
        if gameResult == LOSE_CODE: # 게임을 클리어하지 못한 경우, 게임 1을 호출
            continue

        # 게임 2 스토리 진행
        storyImageList = []
        for storyNum in range(1, 6):
            storyImageList.append("./images/story/story2/story" + str(storyNum) + ".png")

        setAutoUpdate(True)
        story(storyImageList)

        gameResult = game2.game()   # 게임 2를 호출한 뒤 게임 결과를 받음
        if gameResult == LOSE_CODE: # 디멘터 처치, 스니치 획득 조건을 모두 만족하지 못한 경우 게임 1부터 다시 진행
            continue
        else :
            break   # 게임 1, 게임 2를 모두 클리어한 경우 while문을 벗어남

    endWait()   # esc를 누르면 게임 종료
    
def story(storyList):  
    storyImage = makeSprite(storyList[0]) # 첫 스토리 이미지를 생성

    for additionalStory in storyList:   # 나머지 스토리 이미지를 추가
        addSpriteImage(storyImage, additionalStory)

    showSprite(storyImage)

    # 스토리 이미지 오른쪽 하단에 깜빡 거리는 이미지를 추가
    blink = makeSprite("./images/etc/blink1.png")
    moveSprite(blink, 650, 520)
    addSpriteImage(blink, "./images/etc/blink2.png")
    showSprite(blink)

    cur_story_index = 1 # 현재 진행중인 스토리 index
    cur_blink = 0   # 현재 진행중인 blink index
    nextFrame = clock()   # blink 이미지를 바꾸는 시간
    FRAME_UPDATE_INTERVAL = 500 # blink 이미지를 바꾸는 간격

    while cur_story_index <= len(storyList):
        if clock() > nextFrame: # blink 이미지를 바꿀 시간이 되면
            nextFrame += FRAME_UPDATE_INTERVAL  # nextFrame을 update
            cur_blink = (cur_blink + 1) % 2 # index를 바꿔줌 (cur_blink는 0, 1을 반복함)
            changeSpriteImage(blink,  cur_blink)    # 이미지 변경

        if keyPressed("return") or mousePressed():  # 엔터나 마욱스를 클릭한 경우 다음 스토리를 진행
            if cur_story_index == len(storyList):
                break
            else :
                cur_story_index += 1
                changeSpriteImage(storyImage, cur_story_index)  # 다음 스토리 이미지로 변경
                nextFrame += 300
                pause(300)  # enter를 클릭했을 때나 마우스를 클릭했을 때 while문을 반복하면서 여러번 실행되기 때문에 바로 다음 스토리로 넘어가기 위해 지연을 줌

    hideAll()   # 스토리 이미지를 숨김

__main__()