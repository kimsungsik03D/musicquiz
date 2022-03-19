import React from 'react';
import { Link } from 'react-router-dom';

export default function Main(){
  return(
    <div>
      <div>메인페이지입니다.</div>
      <div><Link to={'SoloInGameInterface'}>인게임</Link></div>
    </div>
  )
}