"use client"

import { signIn, signOut, useSession } from "next-auth/react"

export default function Home() {
  const { data: session } = useSession()

  return (
    <div className="grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 font-[var(--font-geist-sans)]">
      <main className="flex flex-col gap-8 row-start-2 items-center sm:items-start">
        <h1 className="text-4xl font-bold">AI Code Review Agent</h1>
        <p className="text-lg text-zinc-600 dark:text-zinc-400">자동으로 PR을 분석하고 리뷰를 제공하는 서비스입니다.</p>
        
        {session ? (
          <div className="flex flex-col gap-4 items-center sm:items-start">
            <p className="text-xl font-medium">안녕하세요, {session.user?.name}님!</p>
            <button
              onClick={() => signOut()}
              className="px-6 py-2 bg-red-500 text-white rounded-full hover:bg-red-600 transition-colors font-medium"
            >
              로그아웃
            </button>
          </div>
        ) : (
          <button
            onClick={() => signIn("github")}
            className="px-6 py-2 bg-black text-white rounded-full hover:bg-gray-800 transition-colors font-medium dark:bg-white dark:text-black"
          >
            GitHub으로 로그인
          </button>
        )}
      </main>
    </div>
  )
}
