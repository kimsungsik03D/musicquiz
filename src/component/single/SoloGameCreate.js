import React from 'react'
import './SoloInGameCreate.css'

const SoloGameCreate = () => {
    return (
        <div className={'mainContainer'}>
            <div className={'SoloHeader'}>
                <div>솔로</div>
                <div>사용자이름</div>
                <div>년도 - 년도</div>
            </div>
            <div className={'container'}>
                <div className={'setting1'}>
                    <div>
                        <div>노래년도 설정</div>
                        <div>
                            <input type={'text'} placeholder={'초기년도'} /> -{' '}
                            <input type={'text'} placeholder={'후기년도'} />
                        </div>
                    </div>
                    <div>
                        <div>힌트 설정</div>
                        <div>
                            노래 가수 힌트 : <input type={'radio'} /> 공개 <input type={'radio'} />{' '}
                            비공개
                        </div>
                        <div>
                            노래 초성 힌트 : <input type={'radio'} /> 공개 <input type={'radio'} />{' '}
                            비공개
                        </div>
                    </div>
                </div>
                <div className={'setting2'}>
                    <div>
                        <div>문제 개수 설정</div>
                        <div>
                            <input type={'text'} placeholder={'문제 갯수 입력'} /> 개
                        </div>
                    </div>
                </div>
                <div>
                    <input type={'button'} value={'게임시작'} />
                </div>
            </div>
        </div>

        //////////////////////////////////////////////////////////////////

        /*       <div>
            <div>
                <div>
                    <div>솔로</div>
                    <div>사용자이름</div>
                    <div>
                        {/!*아마 이 두개는 삭제 가능하지 않을까?*!/}
                        <span>문제년도</span>
                        <span>문제년도</span>
                    </div>
                </div>
                <div>
                    <div>
                        <div>노래년도 설정</div>
                        <div>
                            <input type={'text'} placeholder={'초기년도'} /> -{' '}
                            <input type={'text'} placeholder={'후기년도'} />
                        </div>
                    </div>
                    <div>
                        <div>힌트 설정</div>
                        <div>
                            노래 가수 힌트 : <input type={'radio'} /> 공개 <input type={'radio'} />{' '}
                            비공개
                        </div>
                        <div>
                            노래 초성 힌트 : <input type={'radio'} /> 공개 <input type={'radio'} />{' '}
                            비공개
                        </div>
                    </div>
                    <div>
                        <div>문제 개수 설정</div>
                        <div>
                          <input type={'text'} placeholder={'문제 갯수 입력'} /> 개
                        </div>
                    </div>
                  <div>
                    <input type={'button'} value={'게임시작'}/>
                  </div>
                </div>
            </div>
        </div>*/
    )
}

export default SoloGameCreate
