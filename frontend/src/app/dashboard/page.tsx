"use client"

import { useSession } from "next-auth/react"
import { useEffect, useState } from "react"
import { redirect } from "next/navigation"

interface Repository {
  id: number
  fullName: string
  isActive: boolean
}

export default function Dashboard() {
  const { data: session, status } = useSession()
  const [repos, setRepos] = useState<Repository[]>([])

  useEffect(() => {
    if (status === "unauthenticated") {
      redirect("/")
    }
    console.log(session)

    // // Mock fetch
    // setRepos([
    //   { id: 1, fullName: "user/repo1", isActive: true },
    //   { id: 2, fullName: "user/repo2", isActive: false },
    // ])
  }, [status])

  if (status === "loading") return <p className="p-8">로딩 중...</p>

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-8">내 레포지토리</h1>
      <div className="bg-white dark:bg-zinc-900 shadow rounded-lg overflow-hidden border border-zinc-200 dark:border-zinc-800">
        <ul className="divide-y divide-zinc-200 dark:divide-zinc-800">
          {repos.map((repo) => (
            <li key={repo.id} className="p-4 flex items-center justify-between">
              <div>
                <p className="font-medium text-lg">{repo.fullName}</p>
                <p className="text-sm text-zinc-500">
                  AI 리뷰 {repo.isActive ? "활성화됨" : "비활성화됨"}
                </p>
              </div>
              <button
                className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                  repo.isActive
                    ? "bg-zinc-100 text-zinc-900 hover:bg-zinc-200 dark:bg-zinc-800 dark:text-zinc-100"
                    : "bg-black text-white hover:bg-zinc-800 dark:bg-white dark:text-black"
                }`}
              >
                {repo.isActive ? "비활성화" : "활성화"}
              </button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}
